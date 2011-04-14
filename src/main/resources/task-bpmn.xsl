<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:drools="http://www.jboss.org/drools"
	xmlns:jpdl="urn:jbpm.org:jpdl-3.2" xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL">

	<!-- Import the pieces of jPDL we need. -->
	<xsl:import href="event-bpmn.xsl"/>
	<xsl:import href="transition-bpmn.xsl"/>
	<xsl:import href="action-bpmn.xsl"/>

	<xsl:template match="jpdl:task-node">
	
		<!--  In case of an event, we will use Java Nodes from project -->
		<!--  to process the handler classes.  -->
		<xsl:choose>
		
			<xsl:when test="(jpdl:event) or (jpdl:action)">
				<task>
					<xsl:attribute name="id">
						<xsl:value-of select="translate(@name,' ','_')" />
					</xsl:attribute>
					<xsl:attribute name="name">
						 <xsl:value-of select="@name" />
					</xsl:attribute>
					<xsl:attribute name="drools:taskName">
			    		<xsl:text>JavaNode</xsl:text>
					</xsl:attribute>
					
					<ioSpecification>
						<dataInput>
							<xsl:attribute name="id">
								<xsl:value-of select="translate(@name,' ','_')" />
								<xsl:text>_classInput</xsl:text>
							</xsl:attribute>
							<xsl:attribute name="name">
								<xsl:text>class</xsl:text>
				            </xsl:attribute>	
			            </dataInput>
						<dataInput>
							<xsl:attribute name="id">
								<xsl:value-of select="translate(@name,' ','_')" />
								<xsl:text>_methodInput</xsl:text>
							</xsl:attribute>
							<xsl:attribute name="name">
								<xsl:text>method</xsl:text>
				            </xsl:attribute>	
			            </dataInput>
			            <inputSet>
			            	<dataInputRefs>
			            		<xsl:value-of select="translate(@name,' ','_')" />
								<xsl:text>_classInput</xsl:text>
			            	</dataInputRefs>
			            	<dataInputRefs>
			            		<xsl:value-of select="translate(@name,' ','_')" />
								<xsl:text>_methodInput</xsl:text>
			            	</dataInputRefs>
			            </inputSet>
			            <outputSet />
					</ioSpecification>
					<dataInputAssociation>
					 	<targetRef>
					 		<xsl:value-of select="translate(@name,' ','_')" />
							<xsl:text>_classInput</xsl:text>
						</targetRef>
						<assignment>
							<from>
								<xsl:choose>
									<xsl:when test="jpdl:event">
										<xsl:apply-templates select="jpdl:event" mode="classname"/>
									</xsl:when>
									
									<xsl:when test="jpdl:action">
										<xsl:apply-templates select="jpdl:action"/>
									</xsl:when>
								</xsl:choose>
							</from>
							<to>
								<xsl:value-of select="@name" />
								<xsl:text>_classInput</xsl:text>
							</to>
			        	</assignment>
					</dataInputAssociation>
					<dataInputAssociation>
					 	<targetRef>
					 		<xsl:value-of select="translate(@name,' ','_')" />
							<xsl:text>_methodInput</xsl:text>
						</targetRef>
						<assignment>
							<from>
								<xsl:text>execute</xsl:text>
							</from>
							<to>
								<xsl:value-of select="@name" />
								<xsl:text>_methodInput</xsl:text>
							</to>
			        	</assignment>
					</dataInputAssociation>
				</task>			
			</xsl:when>
			
			<xsl:otherwise>
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
			</xsl:otherwise>
		
		</xsl:choose>
	

	

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
