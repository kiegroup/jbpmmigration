<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:jpdl="urn:jbpm.org:jpdl-3.2"
	xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL">

	<xsl:template match="jpdl:task">
		<xsl:variable name="id">
			<xsl:value-of select="@name" />
		</xsl:variable>
		<userTask>
			<xsl:attribute name="id">
					<xsl:value-of select="@name" />
				</xsl:attribute>
			<xsl:attribute name="name">
			    	<xsl:value-of select="@name" />
			    </xsl:attribute>
			<xsl:if test="@assignee">
				<potentialOwner>
					<resourceAssignmentExpression>
						<formalExpression>
							<xsl:value-of select="@assignee" />
						</formalExpression>
					</resourceAssignmentExpression>
				</potentialOwner>
			</xsl:if>
		</userTask>

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

</xsl:stylesheet>
