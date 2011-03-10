<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:jpdl="urn:jbpm.org:jpdl-3.2"
	xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL">

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
		
		
		<xsl:for-each select="jpdl:transition">
			<sequenceFlow>
				<xsl:attribute name="id">
	        		<xsl:text>flow_</xsl:text>
	        		<xsl:value-of select="../@name" />
	        		<xsl:value-of select='position()' />
	        	</xsl:attribute>
				<xsl:attribute name="sourceRef">
	                <xsl:value-of select="../@name" />
	            </xsl:attribute>
				<xsl:attribute name="targetRef">
	                <xsl:value-of select="@to" />
	            </xsl:attribute>
			</sequenceFlow>
		</xsl:for-each>

	</xsl:template>

	<!-- Processing decision handler elements. -->
    <xsl:template match="jpdl:handler">
    	<documentation>
    		<xsl:text>This decision makes use of the following handler: </xsl:text>
    		<xsl:value-of select="@class" />
    	</documentation>
    </xsl:template>
    
</xsl:stylesheet>
