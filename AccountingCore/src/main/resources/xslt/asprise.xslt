<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:math="http://www.w3.org/2005/xpath-functions/math"
	xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl"
	xmlns:ext="http://AccountingCore.XSLTGenerator.com"
	exclude-result-prefixes="xs math xd" version="3.0">
	<xd:doc scope="stylesheet">
		<xd:desc>

		</xd:desc>
	</xd:doc>

	<xd:doc>
		<xd:desc> Remove blank lines after deleting nodes </xd:desc>
	</xd:doc>
	<xsl:strip-space elements="*" />

	<xsl:template match="*">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>

	
	<xsl:template match="@*">
		<xsl:attribute name="{lower-case(local-name())}">
            <xsl:value-of select="." />
        </xsl:attribute>
	</xsl:template>
	
	<xd:doc>
		<xd:desc> Change asprise-ocr tag to document tag </xd:desc>
	</xd:doc>
	<xsl:template match="asprise-ocr">
		<document>
			<xsl:apply-templates select="@*|node()" />
		</document>
	</xsl:template>


	<xd:doc>
		<xd:desc> Transform block element, the transformation is not the same
			if the block element ancestor's is a table element </xd:desc>
		<xd:desc> if it's ancestor is a table, transform it to text element...
		</xd:desc>
		<xd:desc> if not, transform it to block element with an attribute
			blockType and0 "text" as the value ... </xd:desc>
	</xd:doc>
	<xsl:template match="block">
		<xsl:choose>
			<xsl:when test="ancestor::table">
				<text>
					<par>
						<line l="{@x}" t="{@y}" r="{@x + @width}" b="{@y + @height}">
							<formatting l="{@x}" t="{@y}" r="{@x + @width}"
								b="{@y + @height}" ff="{@font_name}" fs="{@font_size}" lang="FrenchStandard" content="{text()}">
								<xsl:call-template name="letters">
									<xsl:with-param name="text" select="text()" />
									<xsl:with-param name="left" select="@x" />
									<xsl:with-param name="top" select="@y" />
									<xsl:with-param name="right" select="@x + @width" />
									<xsl:with-param name="bottom" select="@y + @height" />
									<xsl:with-param name="step" select="@width div string-length(text())" />
								</xsl:call-template>
							</formatting>
						</line>
					</par>
				</text>
			</xsl:when>
			<xsl:otherwise>
				<block blockType="Text" l="{@x}" t="{@y}" r="{@x + @width}"
					b="{@y + @height}">
					<xsl:apply-templates
						select="@*[not(name() = ('x', 'y'))]" />
					<text>
						<par>
							<line l="{@x}" t="{@y}" r="{@x + @width}" b="{@y + @height}">
								<formatting l="{@x}" t="{@y}" r="{@x + @width}"
											b="{@y + @height}" ff="{@font_name}" fs="{@font_size}" lang="FrenchStandard" content="{text()}">
									<xsl:call-template name="letters">
										<xsl:with-param name="text" select="text()" />
										<xsl:with-param name="left" select="@x" />
										<xsl:with-param name="top" select="@y" />
										<xsl:with-param name="right" select="@x + @width" />
										<xsl:with-param name="bottom" select="@y + @height" />
										<xsl:with-param name="step" select="@width div string-length(text())" />
									</xsl:call-template>
								</formatting>
							</line>
						</par>
					</text>
				</block>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xd:doc>
		<xd:desc> Change table tag to block tag with blockType="Table"
		</xd:desc>
	</xd:doc>
	<xsl:template match="table">
		<block blockType="Table" l="{@x}" t="{@y}" r="{@x + @width}"
			b="{@y + @height}">

			<xsl:for-each-group select="cell" group-by="@row">
				<row>
					<xsl:apply-templates select="current-group()" />
				</row>
			</xsl:for-each-group>
		</block>
	</xsl:template>

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

	<xd:doc>
		<xd:desc> Group cell tags based on their rows attribute </xd:desc>
		<xd:desc> Cells having the same rows value will be grouped and put in
			a single row tag </xd:desc>
	</xd:doc>
	<xsl:template match="cell">
		<cell l="{@x}" t="{@y}" r="{@x + @width}" b="{@y + @height}">

			<xsl:apply-templates
				select="@*[not(name() = ('x', 'y'))]" />
			<xsl:apply-templates select="node()" />
		</cell>
	</xsl:template>

	<!-- Identity transform -->
	<xsl:mode on-no-match="shallow-copy" />




</xsl:stylesheet>