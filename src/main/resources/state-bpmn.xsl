<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:drools="http://www.jboss.org/drools"
  xmlns:jpdl="urn:jbpm.org:jpdl-3.2" xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL">

  <!-- Import the pieces of jPDL we need. -->
  <xsl:import href="event-bpmn.xsl" />
  <xsl:import href="transition-bpmn.xsl" />
  <xsl:import href="action-bpmn.xsl" />


  <xsl:template match="jpdl:state">

    <!-- In case of an event, we will use Java Nodes from project -->
    <!-- to process the handler classes. -->
    <xsl:choose>

    <xsl:when test="(jpdl:event) and (count(jpdl:event) > 1)">
        <task>
          <xsl:attribute name="id">
			<xsl:value-of select="translate(@name,' ','_')" />
	      </xsl:attribute>
          <xsl:attribute name="name">
          	<xsl:text>Expanded to execute: </xsl:text>
			<xsl:value-of select="@name" />
          	<xsl:text> enter</xsl:text>
          </xsl:attribute>
          <xsl:attribute name="drools:taskName">
			<xsl:text>JavaNode</xsl:text>
		  </xsl:attribute>

          <xsl:if test="jpdl:description">
            <xsl:apply-templates select="jpdl:description" />
          </xsl:if>

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
                    <xsl:apply-templates select="jpdl:event" mode="enter" />
                  </xsl:when>

                  <xsl:when test="jpdl:action">
                    <xsl:apply-templates select="jpdl:action" />
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
        
        <!-- Inserting sequence flow from eventNodeEnter to intermediateCatchEvent. -->
        <sequenceFlow>
          <xsl:attribute name="id">
		  	<xsl:text>flow_</xsl:text>
		  	<xsl:value-of select="translate(@name,' ','_')" />
		  	<xsl:value-of select="position()" />
		  </xsl:attribute>
          <xsl:attribute name="sourceRef">
		    <xsl:value-of select="translate(@name,' ','_')" />
		  </xsl:attribute>
          <xsl:attribute name="targetRef">
			<xsl:text>signal: </xsl:text>
			<xsl:value-of select="translate(@name,' ','_')" />
		  </xsl:attribute>
        </sequenceFlow>
        
    	<intermediateCatchEvent>
    	   <xsl:attribute name="id">
    	    <xsl:text>signal: </xsl:text>
			<xsl:value-of select="translate(@name,' ','_')" />
	      </xsl:attribute>
          <xsl:attribute name="name">
            <xsl:text>signal: </xsl:text>
			<xsl:value-of select="@name" />
          </xsl:attribute>

          <xsl:if test="jpdl:description">
            <xsl:apply-templates select="jpdl:description" />
          </xsl:if>
      	  <signalEventDefinition>
      	  	<xsl:attribute name="signalRef">
      	    	<xsl:text>signal</xsl:text>
      	  	</xsl:attribute>
      	  </signalEventDefinition>	
    	</intermediateCatchEvent>
        
        
         <!-- Inserting sequence flow from eventNodeEnter to eventNodeLeave tasks. -->
        <sequenceFlow>
          <xsl:attribute name="id">
		  	<xsl:text>flow_</xsl:text>
		  	<xsl:value-of select="translate(@name,' ','_')" />
		  	<xsl:value-of select="position()+1" />
		  </xsl:attribute>
          <xsl:attribute name="sourceRef">
            <xsl:text>signal: </xsl:text>
		    <xsl:value-of select="translate(@name,' ','_')" />
		  </xsl:attribute>
          <xsl:attribute name="targetRef">
			<xsl:text>javanode_leavenode_</xsl:text>
			<xsl:value-of select="translate(@name,' ','_')" />
		  </xsl:attribute>
        </sequenceFlow>
                
        <task>
          <xsl:attribute name="id">
			<xsl:text>javanode_leavenode_</xsl:text>
			<xsl:value-of select="translate(@name,' ','_')" />
		  </xsl:attribute>
          <xsl:attribute name="name">
          	<xsl:text>Expanded to execute: </xsl:text>
			<xsl:value-of select="translate(@name,' ','_')" />
          	<xsl:text> leave</xsl:text>
		  </xsl:attribute>
          <xsl:attribute name="drools:taskName">
			<xsl:text>JavaNode</xsl:text>
		  </xsl:attribute>
		  
          <xsl:if test="jpdl:description">
            <xsl:apply-templates select="jpdl:description" />
          </xsl:if>

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
                    <xsl:apply-templates select="jpdl:event" mode="leave" />
                  </xsl:when>

                  <xsl:when test="jpdl:action">
                    <xsl:apply-templates select="jpdl:action" />
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
        
        <xsl:apply-templates select="jpdl:transition" mode='node-leave-event'/>
      </xsl:when>

	  <!--  Just a single event, either node enter or node leave. -->
	  <xsl:when test="(jpdl:event) and (count(jpdl:event) = 1)">

		 <xsl:if test="(jpdl:event)/@type='node-enter'">
		 
		 	<!--  task - sequence - signal - sequence -->
	        <task>
	          <xsl:attribute name="id">
				<xsl:value-of select="translate(@name,' ','_')" />
		      </xsl:attribute>
	          <xsl:attribute name="name">
	          	<xsl:text>Expanded to execute: </xsl:text>
				<xsl:value-of select="@name" />
	          	<xsl:text> enter</xsl:text>
	          </xsl:attribute>
	          <xsl:attribute name="drools:taskName">
				<xsl:text>JavaNode</xsl:text>
			  </xsl:attribute>
	
	          <xsl:if test="jpdl:description">
	            <xsl:apply-templates select="jpdl:description" />
	          </xsl:if>
	
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
	                    <xsl:apply-templates select="jpdl:event" mode="enter" />
	                  </xsl:when>
	
	                  <xsl:when test="jpdl:action">
	                    <xsl:apply-templates select="jpdl:action" />
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
	
			<!-- Inserting sequence flow from eventNodeEnter to intermediateCatchEvent. -->  
	        <sequenceFlow>
	          <xsl:attribute name="id">
			  	<xsl:text>flow_</xsl:text>
			  	<xsl:value-of select="translate(@name,' ','_')" />
			  	<xsl:value-of select="position()" />
			  </xsl:attribute>
	          <xsl:attribute name="sourceRef">
			    <xsl:value-of select="translate(@name,' ','_')" />
			  </xsl:attribute>
	          <xsl:attribute name="targetRef">
				<xsl:text>signal: </xsl:text>
				<xsl:value-of select="translate(@name,' ','_')" />
			  </xsl:attribute>
	        </sequenceFlow>
	        
	    	<intermediateCatchEvent>
	    	   <xsl:attribute name="id">
	    	    <xsl:text>signal: </xsl:text>
				<xsl:value-of select="translate(@name,' ','_')" />
		      </xsl:attribute>
	          <xsl:attribute name="name">
	            <xsl:text>signal: </xsl:text>
				<xsl:value-of select="@name" />
	          </xsl:attribute>
	
	          <xsl:if test="jpdl:description">
	            <xsl:apply-templates select="jpdl:description" />
	          </xsl:if>
	      	  <signalEventDefinition>
	      	  	<xsl:attribute name="signalRef">
	      	    	<xsl:text>signal</xsl:text>
	      	  	</xsl:attribute>
	      	  </signalEventDefinition>	
	    	</intermediateCatchEvent>
	
	        <xsl:apply-templates select="jpdl:transition" mode='signal-leave' />
		 </xsl:if>

		 <xsl:if test="(jpdl:event)/@type='node-leave'">
		 
		 	<!--  signal - sequence - task - sequence -->
	    	<intermediateCatchEvent>
	    	   <xsl:attribute name="id">
				<xsl:value-of select="translate(@name,' ','_')" />
		      </xsl:attribute>
	          <xsl:attribute name="name">
	            <xsl:text>signal: </xsl:text>
				<xsl:value-of select="@name" />
	          </xsl:attribute>
	
	          <xsl:if test="jpdl:description">
	            <xsl:apply-templates select="jpdl:description" />
	          </xsl:if>
	      	  <signalEventDefinition>
	      	  	<xsl:attribute name="signalRef">
	      	    	<xsl:text>signal</xsl:text>
	      	  	</xsl:attribute>
	      	  </signalEventDefinition>	
	    	</intermediateCatchEvent>
	
	        <!-- Inserting sequence flow from intermediateCatchEvent to eventNodeLeave tasks. --> 
	        <sequenceFlow>
	          <xsl:attribute name="id">
			  	<xsl:text>flow_</xsl:text>
			  	<xsl:value-of select="translate(@name,' ','_')" />
			  	<xsl:value-of select="position()+1" />
			  </xsl:attribute>
	          <xsl:attribute name="sourceRef">
			    <xsl:value-of select="translate(@name,' ','_')" />
			  </xsl:attribute>
	          <xsl:attribute name="targetRef">
				<xsl:text>javanode_leavenode_</xsl:text>
				<xsl:value-of select="translate(@name,' ','_')" />
			  </xsl:attribute>
	        </sequenceFlow>
	
	        <task>
	          <xsl:attribute name="id">
				<xsl:text>javanode_leavenode_</xsl:text>
				<xsl:value-of select="translate(@name,' ','_')" />
			  </xsl:attribute>
	          <xsl:attribute name="name">
	          	<xsl:text>Expanded to execute: </xsl:text>
				<xsl:value-of select="translate(@name,' ','_')" />
	          	<xsl:text> leave</xsl:text>
			  </xsl:attribute>
	          <xsl:attribute name="drools:taskName">
				<xsl:text>JavaNode</xsl:text>
			  </xsl:attribute>
			  
	          <xsl:if test="jpdl:description">
	            <xsl:apply-templates select="jpdl:description" />
	          </xsl:if>
	
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
	                    <xsl:apply-templates select="jpdl:event" mode="leave" />
	                  </xsl:when>
	
	                  <xsl:when test="jpdl:action">
	                    <xsl:apply-templates select="jpdl:action" />
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
	        
	        <xsl:apply-templates select="jpdl:transition" mode='node-leave-event'/>
		 </xsl:if>		        
	</xsl:when>
	
    <xsl:otherwise>
        <intermediateCatchEvent>
   	   		<xsl:attribute name="id">
   	    		<xsl:text>signal: </xsl:text>
				<xsl:value-of select="translate(@name,' ','_')" />
      		</xsl:attribute>
         	<xsl:attribute name="name">
           		<xsl:text>signal: </xsl:text>
				<xsl:value-of select="@name" />
         	</xsl:attribute>

         	<xsl:if test="jpdl:description">
           		<xsl:apply-templates select="jpdl:description" />
         	</xsl:if>
     	  	<signalEventDefinition>
     	  		<xsl:attribute name="signalRef">
     	    		<xsl:text>signal</xsl:text>
     	  		</xsl:attribute>
     	  	</signalEventDefinition>	
   		</intermediateCatchEvent>
       
        <xsl:apply-templates select="jpdl:transition" mode="signal-leave"/>
    </xsl:otherwise>

	</xsl:choose>	
  </xsl:template>
</xsl:stylesheet>
