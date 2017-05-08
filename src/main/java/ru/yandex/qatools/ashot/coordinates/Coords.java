package ru.yandex.qatools.ashot.coordinates;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.Gson;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */

public class Coords extends Rectangle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static Set<Coords> intersection(Collection<Coords> coordsPool1, Collection<Coords> coordsPool2) {
		Set<Coords> intersectedCoords = new HashSet<>();
		for (Coords coords1 : coordsPool1) {
			for (Coords coords2 : coordsPool2) {
				Coords intersection = coords1.intersection(coords2);
				if (!intersection.isEmpty()) {
					intersectedCoords.add(intersection);
				}
			}
		}
		return intersectedCoords;
	}

	public static Set<Coords> setReferenceCoords(Coords reference, Set<Coords> coordsSet) {
		Set<Coords> referencedCoords = new HashSet<>();
		for (Coords coords : coordsSet) {
			referencedCoords
					.add(new Coords(coords.x - reference.x, coords.y - reference.y, coords.width, coords.height));
		}
		return referencedCoords;
	}

	public static Coords unity(Collection<Coords> coordsCollection) {
		Coords unity = coordsCollection.iterator().next();
		for (Coords coords : coordsCollection) {
			unity = unity.union(coords);
		}
		return unity;
	}

	public static Coords ofImage(BufferedImage image) {
		return new Coords(image.getWidth(), image.getHeight());
	}

	public Coords(Rectangle rectangle) {
		super(rectangle);
	}

	public Coords(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	public Coords(int width, int height) {
		super(width, height);
	}

	public void reduceBy(int pixels) {
		if (pixels < getWidth() / 2 && pixels < getHeight() / 2) {
			this.x += pixels;
			this.y += pixels;
			this.width -= pixels;
			this.height -= pixels;
		}
	}

	@Override
	public Coords union(Rectangle r) {
		return new Coords(super.union(r));
	}

	@Override
	public Coords intersection(Rectangle r) {
		return new Coords(super.intersection(r));
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
