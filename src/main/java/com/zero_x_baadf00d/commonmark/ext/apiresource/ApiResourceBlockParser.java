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

import org.commonmark.node.Block;
import org.commonmark.parser.block.*;

import java.util.regex.Pattern;

/**
 * ApiResource block parser. This parser will extract ApiResource
 * data from the markdown document.
 *
 * @author Thibault Meyer
 * @since 18.02.27
 */
public class ApiResourceBlockParser extends AbstractBlockParser {

    /**
     * ApiResource bloc start boundary.
     */
    private static final Pattern REGEX_BEGIN = Pattern.compile("^\\{% apiresource %}(.*)?");

    /**
     * ApiResource bloc end boundary.
     */
    private static final Pattern REGEX_END = Pattern.compile("^(.*)?\\{% apiresource %}(\\s.*)?");

    /**
     * The found Api Resource block.
     */
    private final ApiResourceBlock block;

    /**
     * Indicate if the block has been completely parser
     * during the initialization.
     */
    private final boolean completedAtInit;

    /**
     * Build a default instance.
     *
     * @param data Initial data
     */
    ApiResourceBlockParser(final String data) {
        this.block = new ApiResourceBlock();

        this.parseLine(
            data.replace("{% apiresource %}", "")
        );
        this.completedAtInit = data.indexOf("{% apiresource %}") != data.lastIndexOf("{% apiresource %}");
    }

    /**
     * Parse the line.
     *
     * @param line The line to parse
     */
    private void parseLine(final String line) {
        for (final String word : line.split("\\s+")) {
            if (!word.isEmpty()) {
                block.appendChild(
                    new ApiResourceNode(word)
                );
            }
        }
    }

    @Override
    public Block getBlock() {
        return this.block;
    }

    @Override
    public BlockContinue tryContinue(final ParserState parserState) {
        if (this.completedAtInit) {
            return BlockContinue.finished();
        }

        final String line = parserState.getLine().toString();
        if (REGEX_END.matcher(line).matches()) {
            if (line.indexOf("{% apiresource %}") == line.lastIndexOf("{% apiresource %}")
                && line.indexOf("{% apiresource %}") > 0) {
                this.parseLine(line.replace("{% apiresource %}", ""));
            }
            return BlockContinue.finished();
        }

        this.parseLine(line);
        return BlockContinue.atIndex(parserState.getIndex());
    }

    /**
     * ApiResource block parser factory.
     *
     * @author Thibault Meyer
     * @since 18.02.24
     */
    public static class Factory extends AbstractBlockParserFactory {

        @Override
        public BlockStart tryStart(final ParserState state, final MatchedBlockParser matchedBlockParser) {
            final CharSequence line = state.getLine();
            if (REGEX_BEGIN.matcher(line).matches()) {
                return BlockStart.of(
                    new ApiResourceBlockParser(line.toString())
                ).atIndex(
                    state.getNextNonSpaceIndex()
                );
            }
            return BlockStart.none();
        }
    }
}
