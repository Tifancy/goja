/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.ctxbox;

/**
 * <p>
 * The Class type .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-01-19 22:13
 * @since JDK 1.6
 */
public enum ClassType {

    /**
     * Controller Class
     */
    CONTROLLER,

    /**
     * Database activiteReocrd Class
     */
    MODEL,
    /**
     * Crob Job Class
     */
    JOB,
    /**
     * System initialization or at the end of the implementation of the class.
     */
    APP
}
