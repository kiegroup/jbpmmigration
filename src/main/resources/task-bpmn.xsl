<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:jpdl="urn:jbpm.org:jpdl-3.2"
	xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL">

	<xsl:template match="jpdl:task-node">
		<userTask>
			<xsl:attribute name="id">
					<xsl:value-of select="@name" />
				</xsl:attribute>
			<xsl:attribute name="name">
			    	<xsl:value-of select="@name" />
			    </xsl:attribute>
		<ioSpecification>
			<dataInput>
				<xsl:attribute name="id">
					<xsl:value-of select="@name" />
					<xsl:text>_TaskNameInput</xsl:text>
				</xsl:attribute>
				<xsl:attribute name="name">
					<xsl:text>TaskName</xsl:text>
	            </xsl:attribute>	
            </dataInput>
            <inputSet>
            	<dataInputRefs>
            		<xsl:value-of select="@name" />
					<xsl:text>_TaskNameInput</xsl:text>
            	</dataInputRefs>
            </inputSet>
            <outputSet />
		</ioSpecification>
		<dataInputAssociation>
		 	<targetRef>
		 		<xsl:value-of select="@name" />
				<xsl:text>_TaskNameInput</xsl:text>
			</targetRef>
			<assignment>
				<from>
					<xsl:apply-templates select="jpdl:task" mode="task"/>
				</from>
				<to>
					<xsl:value-of select="@name" />
					<xsl:text>_TaskNameInput</xsl:text>
				</to>
        	</assignment>
		</dataInputAssociation>
		<xsl:apply-templates select="jpdl:task" mode="assignment" />	       
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

	<xsl:template match="jpdl:task" mode="task">
		<xsl:value-of select="@name" />
	</xsl:template>

	<xsl:template match="jpdl:task" mode="assignment">
		<xsl:if test="jpdl:assignment/@actor-id">
		<potentialOwner>
			<resourceAssignmentExpression>
				<formalExpression>
					<xsl:apply-templates select="jpdl:assignment" />
				</formalExpression>
			</resourceAssignmentExpression>
		</potentialOwner>
		</xsl:if>
	</xsl:template>

	<xsl:template match="jpdl:assignment">
		<xsl:value-of select="@actor-id" />
	</xsl:template>
	
	
</xsl:stylesheet>
