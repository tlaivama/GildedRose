package fi.oulu.tol.sqat.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import fi.oulu.tol.sqat.GildedRose;
import fi.oulu.tol.sqat.Item;

public class GildedRoseTest {

// Example scenarios for testing
//   Item("+5 Dexterity Vest", 10, 20));
//   Item("Aged Brie", 2, 0));
//   Item("Elixir of the Mongoose", 5, 7));
//   Item("Sulfuras, Hand of Ragnaros", 0, 80));
//   Item("Backstage passes to a TAFKAL80ETC concert", 15, 20));
//   Item("Conjured Mana Cake", 3, 6));

	@Test
	public void testUpdateEndOfDay_AgedBrie_Quality_10_11() {
		// Arrange
		GildedRose store = new GildedRose();
		store.addItem(new Item("Aged Brie", 2, 10) );
		
		// Act
		store.updateEndOfDay();
		
		// Assert
		List<Item> items = store.getItems();
		Item itemBrie = items.get(0);
		assertEquals(11, itemBrie.getQuality());
	}
	
	@Test
	public void testUpdateEndOfDay_ItemSellInDecreases() {
		GildedRose store = new GildedRose();
		store.addItem(new Item("Conjured Mana Cake", 10, 10));
		
		GildedRose.updateEndOfDay();
		
		Item item = store.getItems().get(0);
		assertEquals("Item sellIn did not decrease", 9, item.getSellIn());
	}
    
	@Test
	public void testUpdateEndOfDay_ItemQualityIsNotNegative() {
		GildedRose store = new GildedRose();
		store.addItem(new Item("Conjured Mana Cake", -1, 0));
		
		GildedRose.updateEndOfDay();
		
		Item item = store.getItems().get(0);
		assertEquals("Item quality was negative", 0, item.getQuality());
	}
	
	@Test
	public void testUpdateEndOfDay_AgedBrieIncreasesInQuality() {
		GildedRose store = new GildedRose();
		store.addItem(new Item("Aged Brie", 2, 10));
		
		int oldQuality = store.getItems().get(0).getQuality();
		
		GildedRose.updateEndOfDay();
		
		int newQuality = store.getItems().get(0).getQuality();
		assertTrue("Item quality did not increase", newQuality > oldQuality);
	}
	
	@Test
	public void testUpdateEndOfDay_BackstagePassesIncreaseInQuality() {
		GildedRose store = new GildedRose();
		store.addItem(new Item("Backstage passes to a TAFKAL80ETC concert", 13, 3));
		
		int oldQuality = store.getItems().get(0).getQuality();
		
		GildedRose.updateEndOfDay();
		
		int newQuality = store.getItems().get(0).getQuality();
		assertTrue("Item quality did not increase", newQuality > oldQuality);
	}
	
	@Test
	public void testUpdateEndOfDay_BackstagePassesIncreaseInQualityBy2WhenSellInIsLessThan11() {
		GildedRose store = new GildedRose();
		store.addItem(new Item("Backstage passes to a TAFKAL80ETC concert", 10, 3));
		
		int oldQuality = store.getItems().get(0).getQuality();
		
		GildedRose.updateEndOfDay();
		
		int newQuality = store.getItems().get(0).getQuality();
		assertEquals("Item quality did not increase by 2", newQuality, oldQuality + 2);
	}
	
	@Test
	public void testUpdateEndOfDay_BackstagePassesIncreaseInQualityBy3WhenSellInIsLessThan6() {
		GildedRose store = new GildedRose();
		store.addItem(new Item("Backstage passes to a TAFKAL80ETC concert", 5, 3));
		
		int oldQuality = store.getItems().get(0).getQuality();
		
		GildedRose.updateEndOfDay();
		
		int newQuality = store.getItems().get(0).getQuality();
		assertEquals("Item quality did not increase by 3", newQuality, oldQuality + 3);
	}
	
	@Test
	public void testUpdateEndOfDay_BackstagePassesQualityDecreasesTo0AfterConcert() {
		GildedRose store = new GildedRose();
		store.addItem(new Item("Backstage passes to a TAFKAL80ETC concert", 0, 100));
		
		GildedRose.updateEndOfDay();
		
		int quality = store.getItems().get(0).getQuality();
		assertEquals("Item quality did not decrease to 0", quality, 0);
	}
	
	@Test
	public void testUpdateEndOfDay_QualityDegradesTwiceAsFastWhenSellInExpires() {
		GildedRose store = new GildedRose();
		int initialQuality = 10;
		store.addItem(new Item("Conjured Mana Cake", 1, initialQuality));
		
		GildedRose.updateEndOfDay();
		int firstQuality = store.getItems().get(0).getQuality();
		
		GildedRose.updateEndOfDay();
		int secondQuality = store.getItems().get(0).getQuality();
		
		assertEquals("Item quality did not degrade twice as fast", (firstQuality - secondQuality), (initialQuality - firstQuality) * 2);
	}
	
	@Test
	public void testUpdateEndOfDay_SulfurasQualityIsAlways80() {
		GildedRose store = new GildedRose();
		// Since Sulfuras quality should always be 80, it should be updated to such at the end of the day
		store.addItem(new Item("Sulfuras, Hand of Ragnaros", 0, 0));
		
		GildedRose.updateEndOfDay();
		
		assertEquals("Sulfuras did not have quality of 80", store.getItems().get(0).getQuality(), 80);
	}
	
	@Test
	public void testUpdateEndOfDay_SulfurasQualityNeverAlters() {
		GildedRose store = new GildedRose();
		store.addItem(new Item("Sulfuras, Hand of Ragnaros", -1, 80));
		
		GildedRose.updateEndOfDay();
		
		assertEquals("Sulfuras quality changed", store.getItems().get(0).getQuality(), 80);
	}
	
	@Test
	public void testUpdateEndOfDay_OrdinaryItemQualityIsNeverMoreThan50() {
		GildedRose store = new GildedRose();
		store.addItem(new Item("Backstage passes to a TAFKAL80ETC concert", 10, 50));
		
		GildedRose.updateEndOfDay();
		GildedRose.updateEndOfDay();

		assertEquals("Item quality was over 50", store.getItems().get(0).getQuality(), 50);
	}
}
