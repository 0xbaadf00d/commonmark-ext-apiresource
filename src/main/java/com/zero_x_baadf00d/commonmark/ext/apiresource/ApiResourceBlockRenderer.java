/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 Thibault Meyer
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.zero_x_baadf00d.commonmark.ext.apiresource;

import org.commonmark.node.Node;
import org.commonmark.renderer.NodeRenderer;
import org.commonmark.renderer.html.HtmlNodeRendererContext;
import org.commonmark.renderer.html.HtmlWriter;

import java.util.*;

/**
 * Renderer for ApiResource block.
 *
 * @author Thibault Meyer
 * @since 18.02.27
 */
public class ApiResourceBlockRenderer implements NodeRenderer {

    /**
     * The current Html writer.
     */
    private final HtmlWriter html;

    /**
     * Build a default instance/
     *
     * @param context The renderer context
     */
    public ApiResourceBlockRenderer(final HtmlNodeRendererContext context) {
        this.html = context.getWriter();
    }

    @Override
    public Set<Class<? extends Node>> getNodeTypes() {
        return Collections.singleton(ApiResourceBlock.class);
    }

    @Override
    public void render(final Node node) {
        ApiResourceNode n = (ApiResourceNode) node.getFirstChild();
        if (n != null) {
            if (node instanceof ApiResourceBlock) {

                // Open main block
                final Map<String, String> properties = new HashMap<>();
                properties.put("class", "apiresource " + n.getData().toLowerCase(Locale.ENGLISH));
                html.tag("div", properties);

                // Method
                properties.put("class", "method");
                html.tag("span", properties);
                html.raw(n.getData().toUpperCase(Locale.ENGLISH));
                html.tag("/span");
                html.raw(" ");

                // Path
                properties.put("class", "path");
                html.tag("span", properties);
                n = (ApiResourceNode) n.getNext();
                while (n != null) {
                    if (n.getData().startsWith("{") || n.getData().startsWith(":")) {

                        // Variable
                        properties.put("class", "variable");
                        html.tag("span", properties);
                        html.raw(n.getData());
                        html.tag("/span");
                    } else {

                        // Default path
                        html.raw(n.getData());
                    }
                    n = (ApiResourceNode) n.getNext();
                }
                html.tag("/span");

                // Close main block
                html.tag("/div");
            }
        }
    }
}
