<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:jpdl="urn:jbpm.org:jpdl-3.2"
	xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL">

	<!-- Import the pieces of jPDL we need. -->
	<xsl:import href="event-bpmn.xsl"/>
	<xsl:import href="transition-bpmn.xsl"/>
	

	<xsl:template match="jpdl:task-node">
		<userTask>
			<xsl:attribute name="id">
					<xsl:value-of select="translate(@name,' ','_')" />
				</xsl:attribute>
			<xsl:attribute name="name">
			    	<xsl:value-of select="@name" />
			</xsl:attribute>
			
			<xsl:if test="jpdl:event">
	            <documentation>
           	    	// place holder for the following event action 
           	    	// handlers, migrate the code found here:
           	    	//
        			<xsl:apply-templates select="jpdl:event"/>
    	        </documentation>           
			</xsl:if>

		<ioSpecification>
			<dataInput>
				<xsl:attribute name="id">
					<xsl:value-of select="translate(@name,' ','_')" />
					<xsl:text>_TaskNameInput</xsl:text>
				</xsl:attribute>
				<xsl:attribute name="name">
					<xsl:text>TaskName</xsl:text>
	            </xsl:attribute>	
            </dataInput>
            <inputSet>
            	<dataInputRefs>
            		<xsl:value-of select="translate(@name,' ','_')" />
					<xsl:text>_TaskNameInput</xsl:text>
            	</dataInputRefs>
            </inputSet>
            <outputSet />
		</ioSpecification>
		<dataInputAssociation>
		 	<targetRef>
		 		<xsl:value-of select="translate(@name,' ','_')" />
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

		<xsl:apply-templates select="jpdl:transition" />
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
