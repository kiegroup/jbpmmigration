<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:jpdl="urn:jbpm.org:jpdl-3.2"
	xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL">

	<!-- Import the pieces of jPDL we need. -->
	<xsl:import href="sub-process-bpmn.xsl" />
	<xsl:import href="variable-bpmn.xsl" />
	
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
						<xsl:apply-templates select="jpdl:variable" mode="name" /> 
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
						<xsl:apply-templates select="jpdl:variable" />
		            </xsl:attribute>	
	            </dataOutput>
	            <inputSet>
	            	<dataInputRefs>
	            		<xsl:value-of select="translate(@name,' ','_')" />
						<xsl:text>_</xsl:text>
		            	<xsl:apply-templates select="jpdl:variable" />
		            	<xsl:text>Input</xsl:text>
	            	</dataInputRefs>
	            </inputSet>
	            <outputSet>
	            	<dataOutputRefs>
						<xsl:value-of select="translate(@name,' ','_')" />
						<xsl:text>_</xsl:text>
		            	<xsl:apply-templates select="jpdl:variable" />
		            	<xsl:text>Output</xsl:text>
	            	</dataOutputRefs>
	            </outputSet>
            </ioSpecification>
            <dataInputAssociation>
            	<sourceRef>
            		<xsl:value-of select="translate(@name,' ','_')" />
					<xsl:text>_</xsl:text>
		            <xsl:apply-templates select="jpdl:variable" />
		            <xsl:text>Input</xsl:text>
            	</sourceRef>
            	<targetRef>
            		<xsl:value-of select="translate(@name,' ','_')" />
					<xsl:text>_</xsl:text>
		            <xsl:apply-templates select="jpdl:variable" />
		            <xsl:text>Output</xsl:text>
            	</targetRef>
            </dataInputAssociation>
            <dataOutputAssociation>
            	<sourceRef>
           			<xsl:value-of select="translate(@name,' ','_')" />
					<xsl:text>_</xsl:text>
	            	<xsl:apply-templates select="jpdl:variable" />
	            	<xsl:text>Output</xsl:text>
            	</sourceRef>
            	<targetRef>
            		<xsl:value-of select="translate(@name,' ','_')" />
					<xsl:text>_</xsl:text>
		            <xsl:apply-templates select="jpdl:variable" />
		            <xsl:text>Input</xsl:text>
            	</targetRef>
            </dataOutputAssociation>
		</callActivity>

		<xsl:apply-templates select="jpdl:transition"/>
	</xsl:template>

</xsl:stylesheet>
