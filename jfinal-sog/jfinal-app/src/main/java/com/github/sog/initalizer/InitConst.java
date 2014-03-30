/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.github.sog.initalizer;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-02-12 21:58
 * @since JDK 1.6
 */
public interface InitConst {
    /*App Configurate*/
    String APP      = "app";
    String DEV_MODE = "dev.mode";
    String DOMAIN   = "domain";
    String SECURITY = "security";
    String CACHE    = "cache";
    String JOB      = "job";
    String FLASH    = "flash";

    /* View Configuration. */
    String VIEW_PATH = "view.path";
    String VIEW_TYPE = "view.type";
    String VIEW_404  = "view.404";
    String VIEW_500  = "view.500";

    /* Database Configuration. */
    String DB_URL         = "db.url";
    String DB_USERNAME    = "db.username";
    String DB_PASSWORD    = "db.password";
    String DB_SQLINXML    = "db.sqlinxml";
    String DB_SCRIPT_PATH = "db.script.path";


    /* MongoDB Configuration */
    String MONGO_HOST        = "mongo.host";
    String MONGO_URL         = "mongo.url";
    String MONGO_PORT        = "mongo.port";
    String MONGO_DB          = "mongo.db";
    String MONGO_MORIPH      = "mongo.moriph";
    String MONGO_MORIPH_PKGS = "mongo.moriph_pkgs";

    /* Redis Configuration */
    String REDIS_HOST                           = "redis.host";
    String REDIS_PORT                           = "redis.port";
    String REDIS_PASSWORD                       = "redis.password";
    String REDIS_MAXTOTAL                       = "redis.maxtotal";
    String REDIS_MAXIDLE                        = "redis.maxidle";
    String REDIS_MINIDLE                        = "redis.minidle";
    String REDIS_MINEVICTABLEIDLETIMEMILLIS     = "redis.minevictableidletimemillis";
    String REDIS_NUMTESTSPEREVICTIONRUN         = "redis.numtestsperevictionrun";
    String REDIS_SOFTMINEVICTABLEIDLETIMEMILLIS = "redis.softminevictableidletimemillis";
    String REDIS_TIMEBETWEENEVICTIONRUNSMILLIS  = "redis.timebetweenevictionrunsmillis";
    String REDIS_TESTWHILEIDLE                  = "redis.testwhileidle";
    String REDIS_TESTONRETURN                   = "redis.testonreturn";
    String REDIS_TESTONBORROW                   = "redis.testonborrow";
}
