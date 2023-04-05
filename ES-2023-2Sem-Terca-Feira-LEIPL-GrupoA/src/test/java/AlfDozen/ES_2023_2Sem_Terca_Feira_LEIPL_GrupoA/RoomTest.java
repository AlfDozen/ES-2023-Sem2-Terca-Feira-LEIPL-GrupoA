package AlfDozen.ES_2023_2Sem_Terca_Feira_LEIPL_GrupoA;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoomTest {

	@Test
	final void testRoomInteger() {
		Room room = new Room("ES23", 20);
		assertEquals("ES23", room.getName());
		assertEquals(20, room.getCapacity());
		
		Room roomNull = new Room(null, (Integer)null);
		assertNull(roomNull.getName());
		assertNull(roomNull.getCapacity());
	}
	
	@Test
	final void testRoomIntegerException() {
		IllegalArgumentException exceptionNegative = assertThrows(IllegalArgumentException.class, () -> new Room("ES23", -1));
		assertEquals("Room needs to have a positive number capacity", exceptionNegative.getMessage());
	}
	
	@Test
	final void testRoomString() {
		Room room = new Room("ES23", "20");
		assertEquals("ES23", room.getName());
		assertEquals(20, room.getCapacity());
		
		Room roomNull = new Room(null, (String)null);
		assertNull(roomNull.getName());
		assertNull(roomNull.getCapacity());		
	}
	
	@Test
	final void testRoomStringException() {
		NumberFormatException exceptionNotNumber = assertThrows(NumberFormatException.class, () -> new Room("ES23", "abc"));
		assertEquals("The provided string doesn't correspond to a number", exceptionNotNumber.getMessage());
		
		IllegalArgumentException exceptionNegative = assertThrows(IllegalArgumentException.class, () -> new Room("ES23", "-1"));
		assertEquals("Room needs to have a positive number capacity", exceptionNegative.getMessage());
	}
	
	@Test
	final void testGetName() {
		Room room = new Room("ES23", 20);
		assertEquals("ES23", room.getName());
	}

	@Test
	final void testSetName() {
		Room room = new Room("ES23", 20);
		room.setName("NR22");
		assertEquals("NR22", room.getName());
		room.setName(null);
		assertNull(room.getName());
	}

	@Test
	final void testGetCapacity() {
		Room room = new Room("ES23", 20);
		assertEquals(20, room.getCapacity());
	}

	@Test
	final void testSetCapacityInteger() {
		Room room = new Room("ES23", 20);
		room.setCapacity(33);
		assertEquals(33, room.getCapacity());
		room.setCapacity((Integer)null);
		assertNull(room.getCapacity());
	}
	
	@Test
	final void testSetCapacityIntegerException() {
		Room room = new Room("ES23", 20);
		IllegalArgumentException exceptionNegative = assertThrows(IllegalArgumentException.class, () -> room.setCapacity(-1));
		assertEquals("Room needs to have a positive number capacity", exceptionNegative.getMessage());
	}
	
	@Test
	final void testSetCapacityString() {
		Room room = new Room("ES23", 20);
		room.setCapacity("33");
		assertEquals(33, room.getCapacity());
		room.setCapacity((String)null);
		assertNull(room.getCapacity());
	}
	
	@Test
	final void testSetCapacityStringException() {
		Room room = new Room("ES23", 20);
		NumberFormatException exceptionNotNumber = assertThrows(NumberFormatException.class, () -> room.setCapacity("abc"));
		assertEquals("The provided string doesn't correspond to a number", exceptionNotNumber.getMessage());
		
		IllegalArgumentException exceptionNegative = assertThrows(IllegalArgumentException.class, () -> room.setCapacity("-1"));
		assertEquals("Room needs to have a positive number capacity", exceptionNegative.getMessage());
	}
	
	@Test
	final void testIsComplete() {
		Room room = new Room("ES23", 20);
		assertTrue(room.isComplete());
		room.setName(null);
		assertFalse(room.isComplete());
		room.setCapacity((Integer)null);
		assertFalse(room.isComplete());
		room.setName("WO422");
		assertFalse(room.isComplete());
	}

	@Test
	final void testToString() {
		Room room = new Room("ES23", 20);
		assertEquals("Room ES23 (Capacity 20)", room.toString());
		
		Room roomNull = new Room(null, (Integer)null);
		assertEquals("Room " + Lecture.FORNULL + " (Capacity " + Lecture.FORNULL + ")", roomNull.toString());
	}

}
