<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:jpdl="urn:jbpm.org:jpdl-3.2"
	xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL">

	<xsl:template match="jpdl:process-state">
		<callActivity>
			<xsl:attribute name="name">
				<xsl:value-of select="@name" />
			</xsl:attribute>
			<xsl:attribute name="id">
                <xsl:value-of select="translate(@name,' ', '_')" />
            </xsl:attribute>
			<xsl:attribute name="calledElement">
            	<xsl:apply-templates select="jpdl:sub-process" />
            </xsl:attribute>
            <ioSpecification>
				<dataInput>
					<xsl:attribute name="id">
						<xsl:value-of select="translate(@name,' ','_')" />
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
						<xsl:value-of select="translate(@name,' ','_')" />
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

</xsl:stylesheet>
