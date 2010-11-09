<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                xmlns:jpdl="http://jbpm.org/4.3/jpdl">

<!-- Converts a jPDL4.3 file to a BPMN2 file-->
<xsl:output method="xml" indent="yes" />
    
<xsl:template match="/">
  <definitions id="Definition"
               targetNamespace="http://www.jboss.org/drools"
               typeLanguage="http://www.java.com/javaTypes"
               expressionLanguage="http://www.mvel.org/2.0"
               xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL"
               xmlns:xs="http://www.w3.org/2001/XMLSchema-instance"
               xs:schemaLocation="http://www.omg.org/spec/BPMN/20100524/MODEL BPMN20.xsd"
               xmlns:g="http://www.jboss.org/drools/flow/gpd"
               xmlns:tns="http://www.jboss.org/drools">

    <xsl:apply-templates select="jpdl:process"/>

  </definitions>
</xsl:template>
    
<xsl:template match="jpdl:process">
    <process>
      <xsl:attribute name="id"><xsl:value-of select="@name"/></xsl:attribute>
      <xsl:attribute name="name"><xsl:value-of select="@name"/></xsl:attribute>
      <xsl:apply-templates/>
    </process>
</xsl:template>

<xsl:template match="jpdl:start">
  <xsl:variable name="id"><xsl:value-of select="@name"/></xsl:variable>
  <startEvent>
    <xsl:attribute name="id"><xsl:value-of select="$id"/></xsl:attribute>
    <xsl:attribute name="name"><xsl:value-of select="$id"/></xsl:attribute>
    <xsl:if test="@g">
      <xsl:attribute name="g:x"><xsl:value-of select="substring-before(@g,',')"/></xsl:attribute>
      <xsl:variable name="g2"><xsl:value-of select="substring-after(@g,',')"/></xsl:variable>
      <xsl:attribute name="g:y"><xsl:value-of select="substring-before($g2,',')"/></xsl:attribute>
      <xsl:variable name="g3"><xsl:value-of select="substring-after($g2,',')"/></xsl:variable>
      <xsl:attribute name="g:width"><xsl:value-of select="substring-before($g3,',')"/></xsl:attribute>
      <xsl:attribute name="g:height"><xsl:value-of select="substring-after($g3,',')"/></xsl:attribute>
    </xsl:if>
  </startEvent>
  <xsl:for-each select="jpdl:transition">
    <sequenceFlow>
      <xsl:attribute name="sourceRef"><xsl:value-of select="$id"/></xsl:attribute>
      <xsl:attribute name="targetRef"><xsl:value-of select="@to"/></xsl:attribute>
    </sequenceFlow>
  </xsl:for-each>
</xsl:template>
    
<xsl:template match="jpdl:end">
  <xsl:variable name="id"><xsl:value-of select="@name"/></xsl:variable>
  <endEvent>
    <xsl:attribute name="id"><xsl:value-of select="$id"/></xsl:attribute>
    <xsl:attribute name="name"><xsl:value-of select="$id"/></xsl:attribute>
    <xsl:if test="@g">
      <xsl:attribute name="g:x"><xsl:value-of select="substring-before(@g,',')"/></xsl:attribute>
      <xsl:variable name="g2"><xsl:value-of select="substring-after(@g,',')"/></xsl:variable>
      <xsl:attribute name="g:y"><xsl:value-of select="substring-before($g2,',')"/></xsl:attribute>
      <xsl:variable name="g3"><xsl:value-of select="substring-after($g2,',')"/></xsl:variable>
      <xsl:attribute name="g:width"><xsl:value-of select="substring-before($g3,',')"/></xsl:attribute>
      <xsl:attribute name="g:height"><xsl:value-of select="substring-after($g3,',')"/></xsl:attribute>
    </xsl:if>
    <terminateEventDefinition/>
  </endEvent>
</xsl:template>
    
<xsl:template match="jpdl:task">
  <xsl:variable name="id"><xsl:value-of select="@name"/></xsl:variable>
  <userTask>
    <xsl:attribute name="id"><xsl:value-of select="$id"/></xsl:attribute>
    <xsl:attribute name="name"><xsl:value-of select="$id"/></xsl:attribute>
    <xsl:if test="@g">
      <xsl:attribute name="g:x"><xsl:value-of select="substring-before(@g,',')"/></xsl:attribute>
      <xsl:variable name="g2"><xsl:value-of select="substring-after(@g,',')"/></xsl:variable>
      <xsl:attribute name="g:y"><xsl:value-of select="substring-before($g2,',')"/></xsl:attribute>
      <xsl:variable name="g3"><xsl:value-of select="substring-after($g2,',')"/></xsl:variable>
      <xsl:attribute name="g:width"><xsl:value-of select="substring-before($g3,',')"/></xsl:attribute>
      <xsl:attribute name="g:height"><xsl:value-of select="substring-after($g3,',')"/></xsl:attribute>
    </xsl:if>
    <xsl:if test="@assignee">
      <potentialOwner>
        <resourceAssignmentExpression>
          <formalExpression><xsl:value-of select="@assignee"/></formalExpression>
        </resourceAssignmentExpression>
      </potentialOwner>
    </xsl:if>
  </userTask>
  <xsl:for-each select="jpdl:transition">
    <sequenceFlow>
      <xsl:attribute name="sourceRef"><xsl:value-of select="$id"/></xsl:attribute>
      <xsl:attribute name="targetRef"><xsl:value-of select="@to"/></xsl:attribute>
    </sequenceFlow>
  </xsl:for-each>
</xsl:template>

</xsl:stylesheet>