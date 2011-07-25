<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:jpdl="urn:jbpm.org:jpdl-3.2"
  xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL">

  <xsl:template match="jpdl:transition">
    <sequenceFlow>
      <xsl:attribute name="id">
  		<xsl:text>flow_</xsl:text>
   		<xsl:value-of select="translate(../@name,' ','_')" />
		<xsl:value-of select='position()' />
	  </xsl:attribute>
      <xsl:attribute name="sourceRef">
        <xsl:value-of select="translate(../@name,' ','_')" />
	  </xsl:attribute>
      <xsl:attribute name="targetRef">
	    <xsl:value-of select="translate(@to,' ','_')" />
	  </xsl:attribute>
    </sequenceFlow>
  </xsl:template>

  <xsl:template match="jpdl:transition" mode="start-event-javanode">
    <sequenceFlow>
      <xsl:attribute name="id">
		<xsl:text>flow_</xsl:text>
		<xsl:value-of select="translate(../@name,' ','_')" />
		<xsl:value-of select='position()' />
	  </xsl:attribute>
      <xsl:attribute name="sourceRef">
		<xsl:text>javanode_</xsl:text>
	    <xsl:value-of select="translate(../@name,' ','_')" />
	  </xsl:attribute>
      <xsl:attribute name="targetRef">
	    <xsl:value-of select="translate(@to,' ','_')" />
	  </xsl:attribute>
    </sequenceFlow>
  </xsl:template>

</xsl:stylesheet>
