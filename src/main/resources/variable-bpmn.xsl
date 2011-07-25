<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:jpdl="urn:jbpm.org:jpdl-3.2"
	xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL">

	<!-- Variable element name. -->
	<xsl:template match="jpdl:variable" mode="name">
		<xsl:value-of select="@name" />
	</xsl:template>

	<!-- Variable element mapped name. -->
	<xsl:template match="jpdl:variable">
		<xsl:value-of select="@mapped-name" />
	</xsl:template>

</xsl:stylesheet>
