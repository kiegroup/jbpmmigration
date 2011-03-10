<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:jpdl="urn:jbpm.org:jpdl-3.2"
	xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL">

	<!-- Import the pieces of jPDL we need. -->
	<xsl:import href="handler-bpmn.xsl"/>
	<xsl:import href="transition-bpmn.xsl"/>

	<xsl:template match="jpdl:decision">
		<inclusiveGateway>
			<xsl:attribute name="name">
				<xsl:value-of select="@name" />
			</xsl:attribute>
			<xsl:attribute name="id">
                <xsl:value-of select="@name" />
            </xsl:attribute>
            <xsl:attribute name="gatewayDirection">
            	<xsl:text>Diverging</xsl:text>
            </xsl:attribute>
            
            <xsl:apply-templates select="jpdl:handler" />
                        	
		</inclusiveGateway>
		
		<xsl:apply-templates select="jpdl:transition"/>
	</xsl:template>
    
</xsl:stylesheet>
