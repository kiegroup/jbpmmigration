<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:jpdl="urn:jbpm.org:jpdl-3.2"
	xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL">

	<!-- Import the pieces of jPDL we need. -->
	<xsl:import href="event-bpmn.xsl"/>
	<xsl:import href="transition-bpmn.xsl"/>

	<xsl:template match="jpdl:node">
		<scriptTask>
			<xsl:attribute name="name">
                <xsl:value-of select="@name" />
            </xsl:attribute>
			<xsl:attribute name="id">
                <xsl:value-of select="translate(@name,' ','_')" />
            </xsl:attribute>

			<xsl:if test="jpdl:event">
	            <script>
           	    	// place holder for the following action handlers,
           	    	// so you can migrate the code here:
           	    	//
        			<xsl:apply-templates select="jpdl:event" />
    	        </script>           
			</xsl:if>
		</scriptTask>

		<xsl:apply-templates select="jpdl:transition"/>
	</xsl:template>

</xsl:stylesheet>
