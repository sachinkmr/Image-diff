/**
 * 
 */
package com.sapient.unilever.d2.qa.dgt.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hosts implements AutoCloseable {

	protected static final Logger logger = LoggerFactory.getLogger(Hosts.class);
	private File hosts = null;
	private String hostsPath = "";
	private Map<String, String> entries = null;
	private List<String> hostOtherEntries;
	private static Hosts host = null;

	private Hosts() throws Exception {
		hostOtherEntries = new ArrayList<String>();
		String hostsPath = System.getenv("windir") + "\\system32\\drivers\\etc\\hosts";
		hosts = new File(hostsPath);
		if (!hosts.exists()) {
			throw new Exception("Unable to find hosts file");
		}
		parse();
	}

	/**
	 * Collects the entries in the OS's hosts file and parses it and return its
	 * java object
	 * 
	 * @return Hosts instance
	 * 
	 */
	public static synchronized Hosts getInstance() {
		if (host == null) {
			try {
				host = new Hosts();
			} catch (Exception e) {
				logger.error("Unable to create host file object", e);
			}
		}
		return host;
	}

	/**
	 * Collects the entries in the OS's hosts file and parses them into a map.
	 * Commeants are held in a seperate Array.
	 * 
	 * @return Map of entries
	 * @throws Exception
	 *             if does not have right to read
	 */
	private Map<String, String> parse() throws Exception {
		entries = new LinkedHashMap<String, String>();
		if (!hosts.canRead()) {
			throw new Exception("Do not have permissiont to read file.");
		}
		Scanner in = new Scanner(hosts);
		String crntLine = "";
		String[] split;
		while (in.hasNextLine()) {
			crntLine = in.nextLine();

			/*
			 * All needed info is to the left of the comment # if there is one.
			 * So this checks for a comment and grabs everything to the left of
			 * it.
			 */
			if (crntLine.contains("#")) {
				split = crntLine.split("#");
				if (split.length > 0) {
					if (split[0].length() > 0) {
						crntLine = split[0] = split[0].trim();
					} else {
						crntLine = "";
					}
					/* Adds commment line to Comment list */
					if (split[1].length() > 0) {
						hostOtherEntries.add("#" + split[1]);
					} else {
						hostOtherEntries.add("#");
					}
				} else {
					crntLine = "";
				}
			}

			/*
			 * Checks the remaining text, which is gaurenteed not to have a
			 * comment at this point, to see if there is a delimiter so that two
			 * possible valuebles can be extracted.
			 */
			if (crntLine.contains("\t")) {
				split = crntLine.split("\t");
				for (String str : split) {
					str = str.trim();
				}
				if (split[0].contains("::")) {
					hostOtherEntries.add(split[0] + "\t" + split[1]);
				} else {
					entries.put(split[1], split[0]);
				}
			} else {
				if (crntLine.contains(" ")) {
					split = crntLine.split(" ");
					for (String str : split) {
						str = str.trim();
					}

					if (split[0].contains("::")) {
						hostOtherEntries.add(split[0] + "\t" + split[1]);
					} else {
						entries.put(split[1], split[0]);
					}
				}
			}
		}
		in.close();
		return entries;
	}

	/**
	 * Adds an entry into the programs representation of the 'hosts' file.
	 * 
	 * @param redirect
	 *            The URL that will point to the target
	 * @param target
	 *            The server that redirect is to point to
	 * @return true if successful, false if not
	 * @throws UnknownHostException
	 */
	public boolean add(String redirect, String target) {
		if (entries == null) {
			return false;
		}
		try {
			InetAddress targetAddr = Inet4Address.getByName(target);
			entries.put(redirect, targetAddr.getHostAddress());
			host.persist();
		} catch (Exception e) {
			logger.error("Error in adding host entry", e);
			return false;
		}
		return true;
	}

	/**
	 * Removes an entry from the program representation of the 'hosts' file.
	 * 
	 * @param redirect
	 *            the redirect URL of the entry that is to be removed
	 * @return true if successful
	 */
	public boolean remove(String redirect) {
		if (entries == null) {
			return false;
		}
		entries.remove(redirect);
		try {
			host.persist();
		} catch (Exception e) {
			logger.error("Error in removing host entry", e);
			return false;
		}
		return true;
	}

	/**
	 * Lists all entries in the programs representation of the 'hosts' file.
	 * 
	 * @return a String containing a list of all entries
	 */
	public String printList() {
		if (entries == null) {
			return "Failed to parse hosts file.";
		}
		String listString = "";
		Object[] keys = entries.keySet().toArray();
		for (int i = 0; i < entries.size(); i++) {
			String key = (String) keys[i];
			listString += i + ")\t" + (String) entries.get(key) + "\t" + key + "\n";
		}
		return listString;
	}

	/**
	 * Clears the 'hosts' file and replaces it with the entries that are in the
	 * programs representation of it.
	 * 
	 * @throws Exception
	 */
	private void persist() throws Exception {
		if (!hosts.canWrite()) {
			throw new Exception("Does not have permission to write.");
		}

		/* Clears the hosts file for re-population */
		FileOutputStream erasor = new FileOutputStream(hosts);
		try {
			erasor.write((new String()).getBytes());
			erasor.flush();
		} finally {
			erasor.close();
		}

		Writer out = new BufferedWriter(new PrintWriter(hosts));
		try {
			for (String comment : hostOtherEntries) {
				out.write(comment + "\n");
			}
			out.write("\n");
			Set<String> keys = entries.keySet();
			for (String key : keys) {
				out.write(entries.get(key) + "\t" + key + "\n");
			}
			out.flush();
		} finally {
			out.close();
		}
	}

	public String getHostsPath() {
		return hostsPath;
	}

	public String getComments() {
		String str = "";
		for (String s : hostOtherEntries) {
			str += s + "\n";
		}
		return str;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.AutoCloseable#close()
	 */
	@Override
	public void close() throws Exception {
		host = null;
	}

}