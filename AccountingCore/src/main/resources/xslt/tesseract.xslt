<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        xmlns:xs="http://www.w3.org/2001/XMLSchema"
        xmlns:math="http://www.w3.org/2005/xpath-functions/math"
        xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl"
        exclude-result-prefixes="xs math xd" version="3.0">

    <!--REMOVING USELESS TAGS -->
    <xd:doc>
        <xd:desc> REMOVING USELESS TAGS </xd:desc>
    </xd:doc>
    <xsl:template match="SP" />
    <xsl:template match="Illustration" />
    <!-- Remove blank lines after deleting nodes -->
    <xsl:strip-space elements="*" />

    <xsl:template match="/">
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="*">
        <xsl:copy>
            <xsl:apply-templates select="@* | node()" />
        </xsl:copy>
    </xsl:template>

    <xd:doc>
        <xd:desc> Change all attributes name to lowercase </xd:desc>
    </xd:doc>
    <xsl:template match="@*">
        <xsl:attribute name="{lower-case(local-name())}">
            <xsl:value-of select="." />
        </xsl:attribute>
    </xsl:template>

    <xd:doc>
        <xd:desc> Change Page tag to document tag </xd:desc>
    </xd:doc>
    <xsl:template match="Page">
        <document>
            <xsl:apply-templates select="@*|node()" />
        </document>
    </xsl:template>

    <!-- <xd:doc> <xd:desc> Change PrintSpace tag to page tag </xd:desc> </xd:doc>
		<xsl:template match="PrintSpace"> <page> <xsl:apply-templates select="@*|node()"
		/> </page> </xsl:template> -->
    <xd:doc>
        <xd:desc> Change ComposedBlock tag to block tag </xd:desc>
    </xd:doc>
    <xsl:template match="ComposedBlock">
        <block l="{@HPOS}" t="{@VPOS}" r="{@HPOS + @WIDTH}"
               b="{@VPOS + @HEIGHT}" blockType="Text" blockName="">

            <xsl:apply-templates
                    select="@*[not(name() = ('HPOS', 'VPOS'))]" />
            <region>
                <rect l="{@HPOS}" t="{@VPOS}" r="{@HPOS + @WIDTH}" b="{@VPOS + @HEIGHT}" />
            </region>
            <xsl:apply-templates
                    select="node()" />

        </block>
    </xsl:template>

    <xd:doc>
        <xd:desc> Change TextLine tag to line tag </xd:desc>
    </xd:doc>
    <xsl:template match="TextLine">
        <line l="{@HPOS}" t="{@VPOS}" r="{@HPOS + @WIDTH}"
              b="{@VPOS + @HEIGHT}">

            <xsl:apply-templates
                    select="@*[not(name() = ('HPOS', 'VPOS'))]|node()" />
        </line>
    </xsl:template>

    <xd:doc>
        <xd:desc> Change GraphicalElement tag to rect tag </xd:desc>
    </xd:doc>
    <xsl:template match="GraphicalElement">
        <rect l="{@HPOS}" t="{@VPOS}" r="{@HPOS + @WIDTH}"
              b="{@VPOS + @HEIGHT}">

            <xsl:apply-templates
                    select="@*[not(name() = ('HPOS', 'VPOS'))]|node()" />
        </rect>
    </xsl:template>

    <xd:doc>
        <xd:desc> Change TextBlock tag to text tag with par child tag </xd:desc>
    </xd:doc>
    <xsl:template match="TextBlock">
        <text l="{@HPOS}" t="{@VPOS}" r="{@HPOS + @WIDTH}"
              b="{@VPOS + @HEIGHT}">

            <xsl:apply-templates
                    select="@*[not(name() = ('HPOS', 'VPOS'))]" />
            <par>
                <xsl:apply-templates select="node()" />
            </par>
        </text>
    </xsl:template>
    <!-- <xd:doc> <xd:desc> Change TextBlock tag to text tag with par child
		tag </xd:desc> </xd:doc> <xsl:template match="TextBlock"> <text> <xsl:apply-templates
		select="@*" /> <xsl:attribute name="r"> <xsl:value-of select="@HPOS+@WIDTH"
		/> </xsl:attribute> <xsl:attribute name="b"> <xsl:value-of select="@VPOS+@HEIGHT"
		/> </xsl:attribute> <par> <xsl:apply-templates select="node()" /> </par>
		</text> </xsl:template> -->


    <xd:doc>
        <xd:desc> TEMPLATE TO LOOP THROUGH A STRING AND PRINT EACH CHARACTER
            IN charParams TAG
        </xd:desc>
        <xd:param name="text" />
    </xd:doc>
    <xsl:template name="letters">
        <xsl:param name="text" />
        <xsl:param name="left" />
        <xsl:param name="top" />
        <xsl:param name="right" />
        <xsl:param name="bottom" />
        <xsl:param name="step" />
        <xsl:if test="$text != ''">
            <xsl:variable name="letter"
                          select="substring($text, 1, 1)" />
            <xsl:choose>
                <xsl:when test="substring-after($text, $letter) ='' ">
                    <charParams l="{$left}" t="{$top}" r="{$right}" b="{$bottom}">
                        <xsl:value-of select="$letter" />
                    </charParams>
                </xsl:when>
                <xsl:otherwise>
                    <charParams l="{$left}" t="{$top}" r="{round(number($left)+number($step))}" b="{$bottom}">
                        <xsl:value-of select="$letter" />
                    </charParams>
                </xsl:otherwise>
            </xsl:choose>

            <xsl:call-template name="letters">
                <xsl:with-param name="text"
                                select="substring-after($text, $letter)" />
                <xsl:with-param name="top" select="$top" />
                <xsl:with-param name="bottom" select="$bottom" />
                <xsl:with-param name="left" select="round(number($left)+number($step))" />
                <xsl:with-param name="right" select="$right" />
                <xsl:with-param name="step" select="$step" />
            </xsl:call-template>
        </xsl:if>
    </xsl:template>
    <!-- CALLING THE TEMPLATE AND PASSING @CONTENT ATTRIBUTE AS A PARAMETER -->

    <xd:doc>
        <xd:desc> THIS WILL MAKE US READ CONTENT VALUE AND CONVERT IT TO
            CHARPARAMS FOR ABBY OCR
        </xd:desc>
    </xd:doc>
    <xsl:template match="String">
        <formatting lang="FrenchStandard" ff="Arial" fs="{@HEIGHT}" style="{{395D27FE-5B95-415B-85F4-D4CA71EDE9BC}}">
            <xsl:apply-templates
                    select="@*[not(name() = ('HPOS', 'VPOS'))]|node()" />
            <xsl:call-template name="letters">
                <xsl:with-param name="text" select="@CONTENT" />
                <xsl:with-param name="left" select="@HPOS" />
                <xsl:with-param name="top" select="@VPOS" />
                <xsl:with-param name="right" select="@HPOS + @WIDTH" />
                <xsl:with-param name="bottom" select="@VPOS + @HEIGHT" />
                <xsl:with-param name="step" select="@WIDTH div string-length(@CONTENT)" />
            </xsl:call-template>
        </formatting>
    </xsl:template>

    <!-- Identity transform -->
    <xsl:mode on-no-match="shallow-copy" />

    <xsl:template match="PrintSpace">
        <page>
            <xsl:apply-templates select="@*" />
            <xsl:for-each-group select="*"
                                group-adjacent="boolean(self::GraphicalElement)">
                <xsl:choose>
                    <xsl:when test="current-grouping-key()">
                        <region>
                            <xsl:apply-templates select="current-group()" />
                        </region>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:apply-templates select="current-group()" />
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:for-each-group>
        </page>
    </xsl:template>

</xsl:stylesheet>
