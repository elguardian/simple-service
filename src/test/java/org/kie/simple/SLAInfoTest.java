package org.kie.simple;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class SLAInfoTest {
	
	@Test
	public void testKeyGeneration() {
		SLAInfo info = new SLAInfo(100L,100L);
		String expectedKey = "000000000100::000000000100";
		assertEquals(expectedKey,info.getKey());
	}
}
