/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.client.dataframe.transforms;

import org.elasticsearch.client.dataframe.transforms.pivot.PivotConfig;
import org.elasticsearch.common.ParseField;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.xcontent.ConstructingObjectParser;
import org.elasticsearch.common.xcontent.ToXContentObject;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentParser;

import java.io.IOException;
import java.util.Objects;

import static org.elasticsearch.common.xcontent.ConstructingObjectParser.constructorArg;
import static org.elasticsearch.common.xcontent.ConstructingObjectParser.optionalConstructorArg;

public class DataFrameTransformConfig implements ToXContentObject {

    public static final ParseField ID = new ParseField("id");
    public static final ParseField SOURCE = new ParseField("source");
    public static final ParseField DEST = new ParseField("dest");
    // types of transforms
    public static final ParseField PIVOT_TRANSFORM = new ParseField("pivot");

    private final String id;
    private final SourceConfig source;
    private final DestConfig dest;
    private final PivotConfig pivotConfig;

    public static final ConstructingObjectParser<DataFrameTransformConfig, Void> PARSER =
            new ConstructingObjectParser<>("data_frame_transform", true,
                (args) -> {
                    String id = (String) args[0];
                    SourceConfig source = (SourceConfig) args[1];
                    DestConfig dest = (DestConfig) args[2];
                    PivotConfig pivotConfig = (PivotConfig) args[3];
                    return new DataFrameTransformConfig(id, source, dest, pivotConfig);
                });

    static {
        PARSER.declareString(constructorArg(), ID);
        PARSER.declareObject(constructorArg(), (p, c) -> SourceConfig.PARSER.apply(p, null), SOURCE);
        PARSER.declareObject(constructorArg(), (p, c) -> DestConfig.PARSER.apply(p, null), DEST);
        PARSER.declareObject(optionalConstructorArg(), (p, c) -> PivotConfig.fromXContent(p), PIVOT_TRANSFORM);
    }

    public static DataFrameTransformConfig fromXContent(final XContentParser parser) {
        return PARSER.apply(parser, null);
    }


    public DataFrameTransformConfig(final String id,
                                    final SourceConfig source,
                                    final DestConfig dest,
                                    final PivotConfig pivotConfig) {
        this.id = id;
        this.source = source;
        this.dest = dest;
        this.pivotConfig = pivotConfig;
    }

    public String getId() {
        return id;
    }

    public SourceConfig getSource() {
        return source;
    }

    public DestConfig getDestination() {
        return dest;
    }

    public PivotConfig getPivotConfig() {
        return pivotConfig;
    }

    @Override
    public XContentBuilder toXContent(final XContentBuilder builder, final Params params) throws IOException {
        builder.startObject();
        if (id != null) {
            builder.field(ID.getPreferredName(), id);
        }
        if (source != null) {
            builder.field(SOURCE.getPreferredName(), source);
        }
        if (dest != null) {
            builder.field(DEST.getPreferredName(), dest);
        }
        if (pivotConfig != null) {
            builder.field(PIVOT_TRANSFORM.getPreferredName(), pivotConfig);
        }
        builder.endObject();
        return builder;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        final DataFrameTransformConfig that = (DataFrameTransformConfig) other;

        return Objects.equals(this.id, that.id)
                && Objects.equals(this.source, that.source)
                && Objects.equals(this.dest, that.dest)
                && Objects.equals(this.pivotConfig, that.pivotConfig);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, source, dest, pivotConfig);
    }

    @Override
    public String toString() {
        return Strings.toString(this, true, true);
    }
}
