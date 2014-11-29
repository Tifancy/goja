/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package goja;

import org.junit.Test;

import static org.junit.Assert.*;

public class GojaServerTest {
    @Test
    public void testStart() throws Exception {

      new  GojaServer().start();

    }
}