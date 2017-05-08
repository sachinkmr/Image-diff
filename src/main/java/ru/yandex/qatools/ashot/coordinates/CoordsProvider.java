package ru.yandex.qatools.ashot.coordinates;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="pazone@yandex-team.ru">Pavel Zorin</a>
 */

public abstract class CoordsProvider implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public abstract Coords ofElement(WebDriver driver, WebElement element);

	public Set<Coords> ofElements(WebDriver driver, Iterable<WebElement> elements) {
		Set<Coords> elementsCoords = new HashSet<>();
		for (WebElement element : elements) {
			Coords elementCoords = ofElement(driver, element);
			if (!elementCoords.isEmpty()) {
				elementsCoords.add(elementCoords);
			}
		}
		return Collections.unmodifiableSet(elementsCoords);
	}

	public Set<Coords> ofElements(WebDriver driver, WebElement... elements) {
		return ofElements(driver, Arrays.asList(elements));
	}

	public Set<Coords> locatedBy(WebDriver driver, By locator) {
		return ofElements(driver, driver.findElements(locator));
	}

}
