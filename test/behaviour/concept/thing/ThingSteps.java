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

package com.vaticle.typedb.client.test.behaviour.concept.thing;

import com.vaticle.typedb.client.api.concept.thing.Thing;
import com.vaticle.typedb.client.api.concept.type.ThingType;
import com.vaticle.typedb.client.common.Label;
import com.vaticle.typedb.client.test.behaviour.config.Parameters.RootLabel;
import com.vaticle.typedb.common.collection.Collections;
import com.vaticle.typeql.lang.common.TypeQLToken;
import io.cucumber.java.After;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.HashMap;
import java.util.Map;

import static com.vaticle.typedb.client.test.behaviour.concept.type.thingtype.ThingTypeSteps.get_thing_type;
import static com.vaticle.typedb.client.test.behaviour.connection.ConnectionStepsBase.tx;
import static com.vaticle.typedb.client.test.behaviour.util.Util.assertThrows;
import static com.vaticle.typedb.common.collection.Collections.set;
import static com.vaticle.typeql.lang.common.TypeQLToken.Annotation.KEY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ThingSteps {

    private static Map<String, Thing> things = new HashMap<>();

    public static Thing get(String variable) {
        return things.get(variable);
    }

    public static void put(String variable, Thing thing) {
        things.put(variable, thing);
    }

    @Then("entity/attribute/relation {var} is null: {bool}")
    public void thing_is_null(String var, boolean isNull) {
        if (isNull) assertNull(get(var));
        else assertNotNull(get(var));
    }

    @Then("entity/attribute/relation {var} is deleted: {bool}")
    public void thing_is_deleted(String var, boolean isDeleted) {
        assertEquals(isDeleted, get(var).asRemote(tx()).isDeleted());
    }

    @Then("{root_label} {var} has type: {type_label}")
    public void thing_has_type(RootLabel rootLabel, String var, String typeLabel) {
        ThingType type = get_thing_type(rootLabel, typeLabel);
        assertEquals(type, get(var).getType());
    }

    @When("delete entity:/attribute:/relation: {var}")
    public void delete_thing(String var) {
        Thing thing = get(var);
        Thing.Remote remote = thing.asRemote(tx());
        remote.delete();
    }

    @When("entity/attribute/relation {var} set has: {var}")
    public void thing_set_has(String var1, String var2) {
        get(var1).asRemote(tx()).setHas(get(var2).asAttribute());
    }

    @Then("entity/attribute/relation {var} set has: {var}; throws exception")
    public void thing_set_has_throws_exception(String var1, String var2) {
        assertThrows(() -> get(var1).asRemote(tx()).setHas(get(var2).asAttribute()));
    }

    @When("entity/attribute/relation {var} unset has: {var}")
    public void thing_remove_has(String var1, String var2) {
        get(var1).asRemote(tx()).unsetHas(get(var2).asAttribute());
    }

    @Then("entity/attribute/relation {var} get keys contain: {var}")
    public void thing_get_keys_contain(String var1, String var2) {
        assertTrue(get(var1).asRemote(tx()).getHas(set(KEY)).anyMatch(k -> k.equals(get(var2))));
    }

    @Then("entity/attribute/relation {var} get keys do not contain: {var}")
    public void thing_get_keys_do_not_contain(String var1, String var2) {
        assertTrue(get(var1).asRemote(tx()).getHas(set(KEY)).noneMatch(k -> k.equals(get(var2))));
    }

    @Then("entity/attribute/relation {var} get attributes contain: {var}")
    public void thing_get_attributes_contain(String var1, String var2) {
        assertTrue(get(var1).asRemote(tx()).getHas().anyMatch(k -> k.equals(get(var2))));
    }

    @Then("entity/attribute/relation {var} get attributes\\( ?{type_label} ?) contain: {var}")
    public void thing_get_attributes_contain(String var1, String typeLabel, String var2) {
        assertTrue(get(var1).asRemote(tx()).getHas(
                tx().concepts().getAttributeType(typeLabel)
        ).anyMatch(k -> k.equals(get(var2))));
    }

    @Then("entity/attribute/relation {var} get attributes\\( ?{type_label} ?) as\\( ?boolean ?) contain: {var}")
    public void thing_get_attributes_as_boolean_contain(String var1, String typeLabel, String var2) {
        assertTrue(get(var1).asRemote(tx()).getHas(
                tx().concepts().getAttributeType(typeLabel).asBoolean()
        ).anyMatch(k -> k.equals(get(var2))));
    }

    @Then("entity/attribute/relation {var} get attributes\\( ?{type_label} ?) as\\( ?long ?) contain: {var}")
    public void thing_get_attributes_as_long_contain(String var1, String typeLabel, String var2) {
        assertTrue(get(var1).asRemote(tx()).getHas(
                tx().concepts().getAttributeType(typeLabel).asLong()
        ).anyMatch(k -> k.equals(get(var2))));
    }

    @Then("entity/attribute/relation {var} get attributes\\( ?{type_label} ?) as\\( ?double ?) contain: {var}")
    public void thing_get_attributes_as_double_contain(String var1, String typeLabel, String var2) {
        assertTrue(get(var1).asRemote(tx()).getHas(
                tx().concepts().getAttributeType(typeLabel).asDouble()
        ).anyMatch(k -> k.equals(get(var2))));
    }

    @Then("entity/attribute/relation {var} get attributes\\( ?{type_label} ?) as\\( ?string ?) contain: {var}")
    public void thing_get_attributes_as_string_contain(String var1, String typeLabel, String var2) {
        assertTrue(get(var1).asRemote(tx()).getHas(
                tx().concepts().getAttributeType(typeLabel).asString()
        ).anyMatch(k -> k.equals(get(var2))));
    }

    @Then("entity/attribute/relation {var} get attributes\\( ?{type_label} ?) as\\( ?datetime ?) contain: {var}")
    public void thing_get_attributes_as_datetime_contain(String var1, String typeLabel, String var2) {
        assertTrue(get(var1).asRemote(tx()).getHas(
                tx().concepts().getAttributeType(typeLabel).asDateTime()
        ).anyMatch(k -> k.equals(get(var2))));
    }

    @Then("entity/attribute/relation {var} get attributes do not contain: {var}")
    public void thing_get_attributes_do_not_contain(String var1, String var2) {
        assertTrue(get(var1).asRemote(tx()).getHas().noneMatch(k -> k.equals(get(var2))));
    }

    @Then("entity/attribute/relation {var} get attributes\\( ?{type_label} ?) do not contain: {var}")
    public void thing_get_attributes_do_not_contain(String var1, String typeLabel, String var2) {
        assertTrue(get(var1).asRemote(tx()).getHas(
                tx().concepts().getAttributeType(typeLabel)
        ).noneMatch(k -> k.equals(get(var2))));
    }

    @Then("entity/attribute/relation {var} get attributes\\( ?{type_label} ?) as\\( ?boolean ?) do not contain: {var}")
    public void thing_get_attributes_as_boolean_do_not_contain(String var1, String typeLabel, String var2) {
        assertTrue(get(var1).asRemote(tx()).getHas(
                tx().concepts().getAttributeType(typeLabel).asBoolean()
        ).noneMatch(k -> k.equals(get(var2))));
    }

    @Then("entity/attribute/relation {var} get attributes\\( ?{type_label} ?) as\\( ?long ?) do not contain: {var}")
    public void thing_get_attributes_as_long_do_not_contain(String var1, String typeLabel, String var2) {
        assertTrue(get(var1).asRemote(tx()).getHas(
                tx().concepts().getAttributeType(typeLabel).asLong()
        ).noneMatch(k -> k.equals(get(var2))));
    }

    @Then("entity/attribute/relation {var} get attributes\\( ?{type_label} ?) as\\( ?double ?) do not contain: {var}")
    public void thing_get_attributes_as_double_do_not_contain(String var1, String typeLabel, String var2) {
        assertTrue(get(var1).asRemote(tx()).getHas(
                tx().concepts().getAttributeType(typeLabel).asDouble()
        ).noneMatch(k -> k.equals(get(var2))));
    }

    @Then("entity/attribute/relation {var} get attributes\\( ?{type_label} ?) as\\( ?string ?) do not contain: {var}")
    public void thing_get_attributes_as_string_do_not_contain(String var1, String typeLabel, String var2) {
        assertTrue(get(var1).asRemote(tx()).getHas(
                tx().concepts().getAttributeType(typeLabel).asString()
        ).noneMatch(k -> k.equals(get(var2))));
    }

    @Then("entity/attribute/relation {var} get attributes\\( ?{type_label} ?) as\\( ?datetime ?) do not contain: {var}")
    public void thing_get_attributes_as_datetime_do_not_contain(String var1, String typeLabel, String var2) {
        assertTrue(get(var1).asRemote(tx()).getHas(
                tx().concepts().getAttributeType(typeLabel).asDateTime()
        ).noneMatch(k -> k.equals(get(var2))));
    }

    @Then("entity/attribute/relation {var} get relations\\( ?{scoped_label} ?) contain: {var}")
    public void thing_get_relations_contain(String var1, Label scopedLabel, String var2) {
        assertTrue(get(var1).asRemote(tx()).getRelations(
                tx().concepts().getRelationType(scopedLabel.scope().get()).asRemote(tx()).getRelates(scopedLabel.name())
        ).anyMatch(k -> k.equals(get(var2))));
    }

    @Then("entity/attribute/relation {var} get relations contain: {var}")
    public void thing_get_relations_contain(String var1, String var2) {
        assertTrue(get(var1).asRemote(tx()).getRelations().anyMatch(k -> k.equals(get(var2))));
    }

    @Then("entity/attribute/relation {var} get relations\\( ?{scoped_label} ?) do not contain: {var}")
    public void thing_get_relations_do_not_contain(String var1, Label scopedLabel, String var2) {
        assertTrue(get(var1).asRemote(tx()).getRelations(
                tx().concepts().getRelationType(scopedLabel.scope().get()).asRemote(tx()).getRelates(scopedLabel.name())
        ).noneMatch(k -> k.equals(get(var2))));
    }

    @Then("entity/attribute/relation {var} get relations do not contain: {var}")
    public void thing_get_relations_do_not_contain(String var1, String var2) {
        assertTrue(get(var1).asRemote(tx()).getRelations().noneMatch(k -> k.equals(get(var2))));
    }

    @Then("root\\( ?thing ?) get instances count: {int}")
    public void root_thing_type_get_instances_contain(int count) {
        assertEquals(count, tx().concepts().getRootThingType().asRemote(tx()).getInstances().count());
    }

    @Then("root\\( ?thing ?) get instances contain: {var}")
    public void root_thing_type_get_instances_contain(String var) {
        assertTrue(tx().concepts().getRootThingType().asRemote(tx()).getInstances().anyMatch(i -> i.equals(get(var))));
    }

    @Then("root\\( ?thing ?) get instances is empty")
    public void root_thing_type_get_instances_is_empty() {
        assertEquals(0, tx().concepts().getRootThingType().asRemote(tx()).getInstances().count());
    }

    @After
    public void clear() {
        things.clear();
    }
}
