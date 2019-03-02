/*
 * Copyright (C) 2016 Gson Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.nickac.notisyncreborn.utils;

import com.gilecode.yagson.ReadContext;
import com.gilecode.yagson.WriteContext;
import com.gilecode.yagson.com.google.gson.Gson;
import com.gilecode.yagson.com.google.gson.TypeAdapter;
import com.gilecode.yagson.com.google.gson.TypeAdapterFactory;
import com.gilecode.yagson.com.google.gson.reflect.TypeToken;
import com.gilecode.yagson.com.google.gson.stream.JsonReader;
import com.gilecode.yagson.com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PostConstructAdapterFactory implements TypeAdapterFactory {
    // copied from https://gist.github.com/swankjesse/20df26adaf639ed7fd160f145a0b661a
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        for (Class<?> t = type.getRawType(); (t != Object.class) && (t.getSuperclass() != null); t = t.getSuperclass()) {
            for (Method m : t.getDeclaredMethods()) {
                if (m.isAnnotationPresent(PostConstruct.class)) {
                    m.setAccessible(true);
                    TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
                    return new PostConstructAdapter<>(delegate, m);
                }
            }
        }
        return null;
    }

    final static class PostConstructAdapter<T> extends TypeAdapter<T> {
        private final TypeAdapter<T> delegate;
        private final Method method;

        PostConstructAdapter(TypeAdapter<T> delegate, Method method) {
            this.delegate = delegate;
            this.method = method;
        }

        @Override
        public void write(JsonWriter jsonWriter, T t, WriteContext writeContext) throws IOException {
            delegate.write(jsonWriter, t, writeContext);
        }

        @Override
        public T read(JsonReader jsonReader, ReadContext readContext) throws IOException {
            T result = delegate.read(jsonReader, readContext);
            if (result != null) {
                try {
                    method.invoke(result);
                } catch (IllegalAccessException e) {
                    throw new AssertionError();
                } catch (InvocationTargetException e) {
                    if (e.getCause() instanceof RuntimeException) throw (RuntimeException) e.getCause();
                    throw new RuntimeException(e.getCause());
                }
            }
            return null;
        }
    }
}