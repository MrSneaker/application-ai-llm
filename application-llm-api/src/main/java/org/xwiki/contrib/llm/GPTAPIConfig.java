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
package org.xwiki.contrib.llm;

import com.xpn.xwiki.XWikiContext;
import com.xpn.xwiki.XWikiException;
import com.xpn.xwiki.api.Document;
import com.xpn.xwiki.api.XWiki;
import com.xpn.xwiki.doc.XWikiDocument;
import com.xpn.xwiki.objects.BaseObject;
import com.xpn.xwiki.web.Utils;

import org.slf4j.Logger;
import org.xwiki.context.Execution;

import java.util.List;

public class GPTAPIConfig {
    protected Logger logger;

    GPTAPIConfig() {
        super();
    }

    public List<BaseObject> getConfigObjects() {
        try {
            Execution execution = Utils.getComponent(Execution.class);
            XWikiContext context = (XWikiContext) execution.getContext().getProperty("xwikicontext");
            com.xpn.xwiki.XWiki xwiki = context.getWiki();

            XWikiDocument doc = xwiki.getDocument("AI.Code.AIConfig", context);
            List<BaseObject> configObjects = doc.getObjects("AI.Code.AIConfigClass");
            return configObjects;
        } catch (XWikiException e) {
            logger.error("Error trying to access the config :", e);
            return null;
        }

    }
}