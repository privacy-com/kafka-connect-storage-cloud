/*
 * Copyright 2017 Confluent Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.confluent.connect.s3;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;
import org.apache.kafka.common.config.ConfigDef.Width;

import java.util.Map;

import io.confluent.connect.storage.StorageSinkConnectorConfig;
import io.confluent.connect.storage.common.StorageCommonConfig;
import io.confluent.connect.storage.hive.HiveConfig;
import io.confluent.connect.storage.partitioner.PartitionerConfig;

public class S3SinkConnectorConfig extends StorageSinkConnectorConfig {

  // S3 Group
  public static final String S3_BUCKET_CONFIG = "s3.bucket.name";
  private static final String S3_BUCKET_DOC = "The S3 Bucket.";
  private static final String S3_BUCKET_DISPLAY = "S3 Bucket";

  public static final String SSEA_CONFIG = "s3.ssea.name";
  private static final String SSEA_DOC = "The S3 Server Side Encryption Algorithm.";
  private static final String SSEA_DISPLAY = "S3 Server Side Encryption Algorithm";
  private static final String SSEA_DEFAULT = "";

  public static final String PART_SIZE_CONFIG = "s3.part.size";
  private static final String PART_SIZE_DOC = "The Part Size in S3 Multi-part Uploads.";
  private static final int PART_SIZE_DEFAULT = 5 * 1024 * 1024;
  private static final String PART_SIZE_DISPLAY = "S3 Part Size";

  private final String name;

  private final StorageCommonConfig commonConfig;
  private final HiveConfig hiveConfig;
  private final PartitionerConfig partitionerConfig;

  static {
    {
      final String group = "S3";
      int orderInGroup = 0;
      CONFIG_DEF.define(S3_BUCKET_CONFIG,
                        Type.STRING,
                        Importance.HIGH,
                        S3_BUCKET_DOC,
                        group,
                        ++orderInGroup,
                        Width.MEDIUM,
                        S3_BUCKET_DISPLAY);

      CONFIG_DEF.define(SSEA_CONFIG,
                        Type.STRING,
                        SSEA_DEFAULT,
                        Importance.LOW,
                        SSEA_DOC,
                        group,
                        ++orderInGroup,
                        Width.MEDIUM,
                        SSEA_DISPLAY);

      CONFIG_DEF.define(PART_SIZE_CONFIG,
                        Type.INT,
                        PART_SIZE_DEFAULT,
                        Importance.LOW,
                        PART_SIZE_DOC,
                        group,
                        ++orderInGroup,
                        Width.MEDIUM,
                        PART_SIZE_DISPLAY);
    }
  }

  public S3SinkConnectorConfig(Map<String, String> props) {
    this(CONFIG_DEF, props);
  }

  protected S3SinkConnectorConfig(ConfigDef configDef, Map<String, String> props) {
    super(configDef, props);
    commonConfig = new StorageCommonConfig(props);
    hiveConfig = new HiveConfig(props);
    partitionerConfig = new PartitionerConfig(props);
    this.name = parseName(props);
  }

  public StorageCommonConfig getCommonConfig() {
    return commonConfig;
  }

  public PartitionerConfig getParitionerConfig() {
    return partitionerConfig;
  }

  public HiveConfig getHiveConfig() {
    return hiveConfig;
  }

  public String getBucketName() {
    return getString(S3_BUCKET_CONFIG);
  }

  public String getSSEA() {
    return getString(SSEA_CONFIG);
  }

  public int getPartSize() {
    return getInt(PART_SIZE_CONFIG);
  }

  protected static String parseName(Map<String, String> props) {
    String nameProp = props.get("name");
    return nameProp != null ? nameProp : "S3-sink";
  }

  public String getName() {
    return name;
  }

}
