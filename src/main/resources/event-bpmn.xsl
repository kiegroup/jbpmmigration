<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:jpdl="urn:jbpm.org:jpdl-3.2"
	xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL">

	<!-- Import the pieces of jPDL we need. -->
	<xsl:import href="action-bpmn.xsl"/>

	<!-- Event types 'node-enter' and 'node-leave' are added to the documentation element. -->
    <xsl:template match="jpdl:event">
		<xsl:if test="@type='node-enter'">
			<xsl:text>// node-enter: 
			// </xsl:text>
			<xsl:apply-templates select="jpdl:action" />
			<xsl:text>
			</xsl:text>				
		</xsl:if>		
		<xsl:if test="@type='node-leave'">
			<xsl:text>// node-leave: 
			// </xsl:text>
			<xsl:apply-templates select="jpdl:action" />
			<xsl:text>
			</xsl:text>
		</xsl:if>
    </xsl:template>
    
    <xsl:template match="jpdl:event" mode="classname">
		<xsl:choose>
			<xsl:when test="@type='node-enter'">
				<xsl:apply-templates select="jpdl:action" />							
			</xsl:when>		
		 <xsl:otherwise>
			<xsl:apply-templates select="jpdl:action" />
		</xsl:otherwise>
		</xsl:choose>    	
    </xsl:template>
    
</xsl:stylesheet>
