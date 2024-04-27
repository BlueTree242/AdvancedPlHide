/*
 *  LICENSE
 * AdvancedPlHide
 * -------------
 * Copyright (C) 2021 - 2024 BlueTree242
 * -------------
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 *  END
 */

package dev.bluetree242.advancedplhide.utils;

import com.intellectualsites.http.ContentType;
import com.intellectualsites.http.EntityMapper;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class HTTPRequestMultipartBody {
    // From https://varaprasadh.medium.com/how-to-send-multipart-form-data-requests-using-java-native-httpclient-989f6921dbfa
    private final byte[] bytes;
    private String boundary;

    private HTTPRequestMultipartBody(byte[] bytes, String boundary) {
        this.bytes = bytes;
        this.boundary = boundary;
    }

    public String getBoundary() {
        return boundary;
    }

    public void setBoundary(String boundary) {
        this.boundary = boundary;
    }

    public String getContentType() {
        return "multipart/form-data; boundary=" + this.getBoundary();
    }

    public byte[] getBody() {
        return this.bytes;
    }

    public static class Builder {
        List<MultiPartRecord> parts;

        public Builder() {
            this.parts = new ArrayList<>();
        }

        public Builder addPart(String fieldName, String fieldValue) {
            MultiPartRecord part = new MultiPartRecord();
            part.setFieldName(fieldName);
            part.setContent(fieldValue);
            String DEFAULT_MIMETYPE = "text/plain";
            this.parts.add(part);
            return this;
        }

        public Builder addPart(String fieldName, String fieldValue, String contentType) {
            MultiPartRecord part = new MultiPartRecord();
            part.setFieldName(fieldName);
            part.setContent(fieldValue);
            this.parts.add(part);
            return this;
        }

        public Builder addPart(String fieldName, String fieldValue, String contentType, String fileName) {
            MultiPartRecord part = new MultiPartRecord();
            part.setFieldName(fieldName);
            part.setContent(fieldValue);
            this.parts.add(part);
            return this;
        }

        public HTTPRequestMultipartBody build() throws IOException {
            String boundary = new BigInteger(256, new SecureRandom()).toString();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            for (MultiPartRecord record : parts) {
                out.write(("--" + boundary + "\r\n" + "Content-Disposition: form-data; name=\"" + record.getFieldName()).getBytes(StandardCharsets.UTF_8));
                out.write(("\"\r\n").getBytes(StandardCharsets.UTF_8));
                Object content = record.getContent();
                out.write(("\r\n").getBytes(StandardCharsets.UTF_8));
                out.write(((String) content).getBytes(StandardCharsets.UTF_8));
                out.write("\r\n".getBytes(StandardCharsets.UTF_8));
            }
            out.write(("--" + boundary + "--\r\n").getBytes(StandardCharsets.UTF_8));

            return new HTTPRequestMultipartBody(out.toByteArray(), boundary);
        }

        public static class MultiPartRecord {
            private String fieldName;
            private String content;

            public String getFieldName() {
                return fieldName;
            }

            public void setFieldName(String fieldName) {
                this.fieldName = fieldName;
            }

            public Object getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }
        }
    }

    public static class MultiPartSerializer implements EntityMapper.EntitySerializer<HTTPRequestMultipartBody> {

        @Override
        public byte @NotNull [] serialize(@NotNull HTTPRequestMultipartBody input) {
            return input.getBody();
        }

        @Override
        public ContentType getContentType() {
            return ContentType.STRING_UTF8; // No input... Must overwrite.
        }
    }
}
