/*
 * Copyright (C) 2010 Grupo Integrado de Ingeniería
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.udc.gii.mdb.perception;

import es.udc.gii.mdb.util.config.MDBConfiguration;
import es.udc.gii.mdb.util.xml.ConfigUtilXML;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.configuration.Configuration;

/**
 * Through this class we can access quickly to a reference of the fields of an {@link ActionPerceptionPair}
 *
 * @author GII
 */
public class ActionPerceptionPairMap {

    private static ActionPerceptionPairMap instance;
    private Map<String, Integer> map;
    private Map<String, APPairModule> modules;
    private Map<String, String> type;
    private int strategyLength;
    private List<String> externalTIds;
    private List<String> internalTIds;
    private List<String> strategyTIds;
    private List<String> externalTnIds;
    private List<String> internalTnIds;
    private List<String> satisfactionTnIds;
    
    public enum APPairModule {

        EXTERNAL_T, INTERNAL_T, ACTION, EXTERNAL_T_N, INTERNAL_T_N, SATISFACTION_T_N;
    }

    private ActionPerceptionPairMap() {
        map = new HashMap<>();
        modules = new HashMap<>();
        type = new HashMap<>();
        externalTIds = new ArrayList<>();
        externalTnIds = new ArrayList<>();
        internalTIds = new ArrayList<>();
        internalTnIds = new ArrayList<>();
        strategyTIds = new ArrayList<>();
        satisfactionTnIds = new ArrayList<>();

        Configuration conf = MDBConfiguration.getInstance().subset(ConfigUtilXML.ACTION_PERCEPTION_PAIR_TAG);

        Configuration confPerceptionT = conf.subset(ConfigUtilXML.ACTION_PERCEPTION_PAIR_PERCEPTION_T_TAG);

        Configuration external = confPerceptionT.subset(ConfigUtilXML.ACTION_PERCEPTION_PAIR_EXTERNAL_TAG);
        Configuration internal = confPerceptionT.subset(ConfigUtilXML.ACTION_PERCEPTION_PAIR_INTERNAL_TAG);

        List<String> ids = external.getList(ConfigUtilXML.ACTION_PERCEPTION_PAIR_FIELD_TAG + "." + ConfigUtilXML.ACTION_PERCEPTION_PAIR_ID_TAG);

        int field = 0;

        for (int i = 0; i < ids.size(); i++) {
            map.put(ids.get(i), field++);
            modules.put(ids.get(i), APPairModule.EXTERNAL_T);
            type.put(ids.get(i), external.getString(ConfigUtilXML.ACTION_PERCEPTION_PAIR_FIELD_TAG + "(" + i + ")." + ConfigUtilXML.ACTION_PERCEPTION_PAIR_TYPE_TAG));
            externalTIds.add(ids.get(i));
        }

        field = 0;
        ids = internal.getList(ConfigUtilXML.ACTION_PERCEPTION_PAIR_FIELD_TAG + "." + ConfigUtilXML.ACTION_PERCEPTION_PAIR_ID_TAG);

        for (int i = 0; i < ids.size(); i++) {
            map.put(ids.get(i), field++);
            modules.put(ids.get(i), APPairModule.INTERNAL_T);
            type.put(ids.get(i), internal.getString(ConfigUtilXML.ACTION_PERCEPTION_PAIR_FIELD_TAG + "(" + i + ")." + ConfigUtilXML.ACTION_PERCEPTION_PAIR_TYPE_TAG));
            internalTIds.add(ids.get(i));
        }

        Configuration confStrategy = conf.subset(ConfigUtilXML.ACTION_PERCEPTION_PAIR_STRATEGY_TAG);

        field = 0;
        if (!confStrategy.isEmpty()) {
            strategyLength = confStrategy.getInt(ConfigUtilXML.ACTION_PERCEPTION_PAIR_STRATEGY_LENGTH_TAG);
            ids = confStrategy.getList(ConfigUtilXML.ACTION_PERCEPTION_PAIR_STRATEGY_ACTION_TAG + "."
                    + ConfigUtilXML.ACTION_PERCEPTION_PAIR_FIELD_TAG + "." + ConfigUtilXML.ACTION_PERCEPTION_PAIR_ID_TAG);

            for (int i = 0; i < ids.size(); i++) {
                map.put(ids.get(i), field++);
                modules.put(ids.get(i), APPairModule.ACTION);
                type.put(ids.get(i), confStrategy.getString(ConfigUtilXML.ACTION_PERCEPTION_PAIR_STRATEGY_ACTION_TAG + "." + ConfigUtilXML.ACTION_PERCEPTION_PAIR_FIELD_TAG + "(" + i + ")." + ConfigUtilXML.ACTION_PERCEPTION_PAIR_TYPE_TAG));
                strategyTIds.add(ids.get(i));
            }
        }

        Configuration confPerceptionTn = conf.subset(ConfigUtilXML.ACTION_PERCEPTION_PAIR_PERCEPTION_T_N_TAG);

        external = confPerceptionTn.subset(ConfigUtilXML.ACTION_PERCEPTION_PAIR_EXTERNAL_TAG);
        internal = confPerceptionTn.subset(ConfigUtilXML.ACTION_PERCEPTION_PAIR_INTERNAL_TAG);

        field = 0;
        ids = external.getList(ConfigUtilXML.ACTION_PERCEPTION_PAIR_FIELD_TAG + "." + ConfigUtilXML.ACTION_PERCEPTION_PAIR_ID_TAG);

        for (int i = 0; i < ids.size(); i++) {
            map.put(ids.get(i), field++);
            modules.put(ids.get(i), APPairModule.EXTERNAL_T_N);
            type.put(ids.get(i), external.getString(ConfigUtilXML.ACTION_PERCEPTION_PAIR_FIELD_TAG + "(" + i + ")." + ConfigUtilXML.ACTION_PERCEPTION_PAIR_TYPE_TAG));
            externalTnIds.add(ids.get(i));
        }

        field = 0;
        ids = internal.getList(ConfigUtilXML.ACTION_PERCEPTION_PAIR_FIELD_TAG + "." + ConfigUtilXML.ACTION_PERCEPTION_PAIR_ID_TAG);

        for (int i = 0; i < ids.size(); i++) {
            map.put(ids.get(i), field++);
            modules.put(ids.get(i), APPairModule.INTERNAL_T_N);
            type.put(ids.get(i), internal.getString(ConfigUtilXML.ACTION_PERCEPTION_PAIR_FIELD_TAG + "(" + i + ")." + ConfigUtilXML.ACTION_PERCEPTION_PAIR_TYPE_TAG));
            internalTnIds.add(ids.get(i));
        }

        Configuration internalSt = conf.subset(ConfigUtilXML.ACTION_PERCEPTION_PAIR_SATISFACTION_T_N_TAG);

        field = 0;
        ids = internalSt.getList(ConfigUtilXML.ACTION_PERCEPTION_PAIR_FIELD_TAG + "." + ConfigUtilXML.ACTION_PERCEPTION_PAIR_ID_TAG);

        for (int i = 0; i < ids.size(); i++) {
            map.put(ids.get(i), field++);
            modules.put(ids.get(i), APPairModule.SATISFACTION_T_N);
            type.put(ids.get(i), internalSt.getString(ConfigUtilXML.ACTION_PERCEPTION_PAIR_FIELD_TAG + "(" + i + ")." + ConfigUtilXML.ACTION_PERCEPTION_PAIR_TYPE_TAG));
            satisfactionTnIds.add(ids.get(i));
        }

    }

    /**
     * Implemented as Singleton, this method provides the instance of the class
     *
     * @return
     */
    public static ActionPerceptionPairMap getInstance() {
        if (instance == null) {
            instance = new ActionPerceptionPairMap();
        }
        return instance;
    }

    /**
     * With this method we can locate the part of the action-perception where the field is.
     *
     * @param key Identifier of the field we are looking for.
     * @return Module (part) of the pair where the field is
     */
    public APPairModule getModule(String key) {
        return modules.get(key);
    }

    /**
     * Gets the index of a field, relative to its module.
     *
     * @param key Identifier of the field we are looking for
     * @return Index of the field into its module
     */
    public Integer getRelativeIndex(String key) {
        return map.get(key);
    }

    /**
     * Gets the type of a field (specified at config file).
     *
     * e.g. if we have distance perceptions at instant t and t+n, both of them will be of 'type'
     * distance
     *
     * @param key Identifier of the field we are looking for
     * @return
     */
    public String getType(String key) {
        return type.get(key);
    }

    /**
     * Gets the length of the strategy associated with the action-perception pair, which is
     * specified through the config xml file
     *
     * @return
     */
    public int getStrategyLength() {
        return strategyLength;
    }

    public List<String> getExternalTIds() {
        return externalTIds;
    }

    public List<String> getExternalTnIds() {
        return externalTnIds;
    }

    public List<String> getInternalTIds() {
        return internalTIds;
    }

    public List<String> getInternalTnIds() {
        return internalTnIds;
    }

    public List<String> getSatisfactionTnIds() {
        return satisfactionTnIds;
    }

    public List<String> getStrategyTIds() {
        return strategyTIds;
    }
}
