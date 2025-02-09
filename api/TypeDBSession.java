/*
 * Copyright (C) 2022 Vaticle
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.vaticle.typedb.client.api;

import com.vaticle.typedb.client.api.database.Database;
import com.vaticle.typedb.protocol.SessionProto;

import javax.annotation.CheckReturnValue;

public interface TypeDBSession extends AutoCloseable {

    @CheckReturnValue
    boolean isOpen();

    @CheckReturnValue
    Type type();

    @CheckReturnValue
    Database database();

    @CheckReturnValue
    TypeDBOptions options();

    @CheckReturnValue
    TypeDBTransaction transaction(TypeDBTransaction.Type type);

    @CheckReturnValue
    TypeDBTransaction transaction(TypeDBTransaction.Type type, TypeDBOptions options);

    void onClose(Runnable function);

    void close();

    enum Type {
        DATA(0),
        SCHEMA(1);

        private final int id;
        private final boolean isSchema;

        Type(int id) {
            this.id = id;
            this.isSchema = id == 1;
        }

        public static Type of(int value) {
            for (Type t : values()) {
                if (t.id == value) return t;
            }
            return null;
        }

        public int id() {
            return id;
        }

        public boolean isData() {
            return !isSchema;
        }

        public boolean isSchema() {
            return isSchema;
        }

        public SessionProto.Session.Type proto() {
            return SessionProto.Session.Type.forNumber(id);
        }
    }
}
