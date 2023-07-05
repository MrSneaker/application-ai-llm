/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.contrib.llm.internal;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.slf4j.Logger;
import org.xwiki.component.annotation.Component;
import org.xwiki.contrib.llm.GPTAPIEmbedding;
import org.xwiki.contrib.llm.GPTAPIException;
import org.xwiki.rest.XWikiRestException;
import org.xwiki.stability.Unstable;

import com.github.openjson.JSONObject;
import com.robrua.nlp.bert.Bert;
import com.xpn.xwiki.XWikiException;

import javax.inject.Inject;
import javax.inject.Singleton;

@Component
@Unstable
@Singleton
public class DefaultGPTAPIEmbeddingBERT implements GPTAPIEmbedding{
    @Inject
    protected Logger logger;

    @Override
    public float[] doEmbedding(String data) throws GPTAPIException {
        try (Bert bert = Bert.load("com/robrua/nlp/easy-bert/bert-multi-cased-L-12-H-768-A-12")) {
            float[] embedding = bert.embedSequence(data);
            logger.info("data : ", data);
            return embedding;
        }
    }

    @Override
    public String doEmbeddingREST(String data) throws GPTAPIException {
        try {
            HttpClient client = new HttpClient();
            String url = "https://llmapi.ai.devxwiki.com/v1/embeddings";
            logger.info("Calling url: " + url);
            PostMethod post = new PostMethod(url);

            post.setRequestHeader("Content-Type", "application/json");
            post.setRequestHeader("Accept", "application/json");

            JSONObject request = new JSONObject();
            request.put("input", data);
            request.put("model", "text-embedding-ada-002");
            String jsonInputString = request.toString();
            StringRequestEntity requestEntity = new StringRequestEntity(
                    jsonInputString,
                    "application/json",
                    "UTF-8");

            post.setRequestEntity(requestEntity);

            int statusCode = client.executeMethod(post);
            if (statusCode != HttpStatus.SC_OK) {
                logger.error("Method failed: " + post.getStatusLine());
                throw new XWikiRestException(post.getStatusLine().toString() + post.getStatusText(), null);
            }
            byte[] responseBody = post.getResponseBody();
            return new String(responseBody);
        } catch (Exception e) {
            logger.error("An error occured : ", e);
            e.printStackTrace();
            return e.toString();
        }
    }
}
