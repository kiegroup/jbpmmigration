<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:jpdl="urn:jbpm.org:jpdl-3.2"
	xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL">

	<xsl:output method="xml" />

	<xsl:template match="/">
		<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL"
			xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:dc="http://www.omg.org/spec/DD/20100524/DC"
			xmlns:di="http://www.omg.org/spec/DD/20100524/DI" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			targetNamespace="http://www.jbpm.org/">
			<xsl:attribute name="id">
	      		<xsl:value-of select="jpdl:process-definition/@name" />
      		</xsl:attribute>
			<xsl:attribute name="name">
                <xsl:value-of select="jpdl:process-definition/@name" />
            </xsl:attribute>
			<xsl:apply-templates select="jpdl:process-definition" />
		</definitions>
	</xsl:template>

	<xsl:template match="jpdl:process-definition">
		<process>
			<xsl:attribute name="id">
	      		<xsl:value-of select="@name" /><xsl:text>Process</xsl:text>
      		</xsl:attribute>
			<xsl:attribute name="name">
	      		<xsl:value-of select="@name" />
      		</xsl:attribute>

			<xsl:apply-templates />
		</process>
	</xsl:template>


	<xsl:template match="jpdl:process-state">
		<callActivity>
			<xsl:attribute name="name">
				<xsl:value-of select="@name" />
			</xsl:attribute>
			<xsl:attribute name="id">
                <xsl:value-of select="@name" />
            </xsl:attribute>
			<xsl:attribute name="calledElement">
            	<xsl:apply-templates select="jpdl:sub-process" />
            </xsl:attribute>
            <ioSpecification>
				<dataInput>
					<xsl:attribute name="id">
						<xsl:value-of select="@name" />
						<xsl:text>_</xsl:text>
		            	<xsl:apply-templates select="jpdl:variable" />
		            	<xsl:text>Input</xsl:text>
					</xsl:attribute>
					<xsl:attribute name="name">
						<xsl:apply-templates select="jpdl:variable">
					 		<xsl:with-param name="return">name</xsl:with-param>
						</xsl:apply-templates>
		            </xsl:attribute>	
	            </dataInput>
	            <dataOutput>
					<xsl:attribute name="id">
						<xsl:value-of select="@name" />
						<xsl:text>_</xsl:text>
		            	<xsl:apply-templates select="jpdl:variable" />
		            	<xsl:text>Output</xsl:text>
					</xsl:attribute>
					<xsl:attribute name="name">
						<xsl:apply-templates select="jpdl:variable">
					 		<xsl:with-param name="return">mapped-name</xsl:with-param>
						</xsl:apply-templates>
		            </xsl:attribute>	
	            </dataOutput>
	            <inputSet>
	            	<dataInputRefs>
	            		<xsl:value-of select="@name" />
						<xsl:text>_</xsl:text>
		            	<xsl:apply-templates select="jpdl:variable" />
		            	<xsl:text>Input</xsl:text>
	            	</dataInputRefs>
	            </inputSet>
	            <outputSet>
	            	<dataOutputRefs>
						<xsl:value-of select="@name" />
						<xsl:text>_</xsl:text>
		            	<xsl:apply-templates select="jpdl:variable" />
		            	<xsl:text>Output</xsl:text>
	            	</dataOutputRefs>
	            </outputSet>
            </ioSpecification>
            <dataInputAssociation>
            	<sourceRef>
            		<xsl:value-of select="@name" />
					<xsl:text>_</xsl:text>
		            <xsl:apply-templates select="jpdl:variable" />
		            <xsl:text>Input</xsl:text>
            	</sourceRef>
            	<targetRef>
            		<xsl:value-of select="@name" />
					<xsl:text>_</xsl:text>
		            <xsl:apply-templates select="jpdl:variable" />
		            <xsl:text>Output</xsl:text>
            	</targetRef>
            </dataInputAssociation>
            <dataOutputAssociation>
            	<sourceRef>
           			<xsl:value-of select="@name" />
					<xsl:text>_</xsl:text>
	            	<xsl:apply-templates select="jpdl:variable" />
	            	<xsl:text>Output</xsl:text>
            	</sourceRef>
            	<targetRef>
            		<xsl:value-of select="@name" />
					<xsl:text>_</xsl:text>
		            <xsl:apply-templates select="jpdl:variable" />
		            <xsl:text>Input</xsl:text>
            	</targetRef>
            </dataOutputAssociation>
		</callActivity>

		<xsl:apply-templates select="jpdl:transition"/>
	</xsl:template>

	<!-- Sub-process element supplied the name of the process -->
	<!-- to be called.                                        -->
	<xsl:template match="jpdl:sub-process">
		<xsl:value-of select="@name" />
	</xsl:template>

	<!-- Variable element with a parameter, set to -->
	<!-- variable you want to return.              -->
	<xsl:template match="jpdl:variable">
		<xsl:param name="return" />
		<xsl:choose>
			<xsl:when test="$return='name'">
				<xsl:value-of select="@name" />
			</xsl:when>
			
			<xsl:otherwise>
				<xsl:value-of select="@mapped-name" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="jpdl:start-state">
		<startEvent>
			<xsl:attribute name="name">
                <xsl:value-of select="@name" />
            </xsl:attribute>
			<xsl:attribute name="id">
                <xsl:value-of select="@name" />
            </xsl:attribute>
		</startEvent>

		<xsl:apply-templates />
	</xsl:template>

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


	<xsl:template match="jpdl:end-state">
		<endEvent>
			<xsl:attribute name="name">
                <xsl:value-of select="@name" />
            </xsl:attribute>
			<xsl:attribute name="id">
                <xsl:value-of select="@name" />
            </xsl:attribute>
		</endEvent>
	</xsl:template>

	<xsl:template match="jpdl:node">
		<scriptTask>
			<xsl:attribute name="name">
                <xsl:value-of select="@name" />
            </xsl:attribute>
			<xsl:attribute name="id">
                <xsl:value-of select="@name" />
            </xsl:attribute>
		</scriptTask>

		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="jpdl:state">
		<serviceTask>
			<xsl:attribute name="name">
				<xsl:value-of select="@name" />
			</xsl:attribute>
			<xsl:attribute name="id">
                <xsl:value-of select="@name" />
            </xsl:attribute>
		</serviceTask>

		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="jpdl:decision">
		<complexGateway>
			<xsl:attribute name="name">
				<xsl:value-of select="@name" />
			</xsl:attribute>
			<xsl:attribute name="id">
                <xsl:value-of select="@name" />
            </xsl:attribute>
		</complexGateway>

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


	<xsl:template match="jpdl:transition">
		<sequenceFlow>
			<xsl:attribute name="id">
        		<xsl:text>flow_</xsl:text>
        		<xsl:value-of select="../@name" />
        	</xsl:attribute>
			<xsl:attribute name="sourceRef">
                <xsl:value-of select="../@name" />
            </xsl:attribute>
			<xsl:attribute name="targetRef">
                <xsl:value-of select="@to" />
            </xsl:attribute>
		</sequenceFlow>
	</xsl:template>

	<!-- Strip the white space from the result. -->
	<xsl:template match="text()">
		<xsl:value-of select="normalize-space()" />
	</xsl:template>

</xsl:stylesheet>
