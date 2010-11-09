<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="/process-definition">
        <definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI"
            xmlns:dc="http://www.omg.org/spec/DD/20100524/DC" xmlns:di="http://www.omg.org/spec/DD/20100524/DI" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            targetNamespace="http://www.jbpm.org/">
            <xsl:attribute name="name"> 
                <xsl:value-of select="@name" />
            </xsl:attribute>

            <xsl:apply-templates />
            <xsl:apply-templates select="//transition" />

        </definitions>
    </xsl:template>

    <xsl:template match="//start-state">
        <startEvent>
            <xsl:attribute name="name">
                <xsl:value-of select="@name" />
            </xsl:attribute>
            <xsl:attribute name="id">
                <xsl:value-of select="@name" /><xsl:text>_id</xsl:text>
            </xsl:attribute>
        </startEvent>
    </xsl:template>

    <xsl:template match="//end-state">
        <endEvent>
            <xsl:attribute name="name">
                <xsl:value-of select="@name" />
            </xsl:attribute>
            <xsl:attribute name="id">
                <xsl:value-of select="@name" /><xsl:text>_id</xsl:text>
            </xsl:attribute>
        </endEvent>
    </xsl:template>

    <xsl:template match="//node">
        <serviceTask>
            <xsl:attribute name="name">
                <xsl:value-of select="@name" />
            </xsl:attribute>
            <xsl:attribute name="id">
                <xsl:value-of select="@name" /><xsl:text>_id</xsl:text>
            </xsl:attribute>
        </serviceTask>
    </xsl:template>

    <xsl:template match="//transition">
        <sequenceFlow>
            <xsl:attribute name="sourceRef">
                <xsl:value-of select="../@name" /><xsl:text>_id</xsl:text>
            </xsl:attribute>
            <xsl:attribute name="targetRef">
                <xsl:value-of select="@to" /><xsl:text>_id</xsl:text>
            </xsl:attribute>
        </sequenceFlow>
    </xsl:template>

    <!-- TODO: Include templates for the missing jPDL elements. -->

    <!-- Strip the white space from the result. -->
    <xsl:template match="text()">
        <xsl:value-of select="normalize-space()" />
    </xsl:template>

</xsl:stylesheet>
