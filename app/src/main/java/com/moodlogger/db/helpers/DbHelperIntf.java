package com.moodlogger.db.helpers;

import com.moodlogger.db.entities.Entity;

public interface DbHelperIntf<T extends Entity> {

    long create(T entity);
}
