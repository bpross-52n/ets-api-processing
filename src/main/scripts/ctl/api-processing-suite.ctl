<?xml version="1.0" encoding="UTF-8"?>
<ctl:package xmlns:ctl="http://www.occamlab.com/ctl"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:tns="http://www.opengis.net/cite/api-processing"
  xmlns:saxon="http://saxon.sf.net/"
  xmlns:tec="java:com.occamlab.te.TECore"
  xmlns:tng="java:org.opengis.cite.api.processing.TestNGController">

  <ctl:function name="tns:run-ets-api-processing">
		<ctl:param name="testRunArgs">A Document node containing test run arguments (as XML properties).</ctl:param>
    <ctl:param name="outputDir">The directory in which the test results will be written.</ctl:param>
		<ctl:return>The test results as a Source object (root node).</ctl:return>
		<ctl:description>Runs the api-processing ${version} test suite.</ctl:description>
    <ctl:code>
      <xsl:variable name="controller" select="tng:new($outputDir)" />
      <xsl:copy-of select="tng:doTestRun($controller, $testRunArgs)" />
    </ctl:code>
	</ctl:function>

   <ctl:suite name="tns:ets-api-processing-${version}">
     <ctl:title>Test suite: ets-api-processing</ctl:title>
     <ctl:description>Describe scope of testing.</ctl:description>
     <ctl:starting-test>tns:Main</ctl:starting-test>
   </ctl:suite>
 
   <ctl:test name="tns:Main">
      <ctl:assertion>The test subject satisfies all applicable constraints.</ctl:assertion>
	  <ctl:code>
		<xsl:variable name="form-data">
                <ctl:form height="640" width="800">
                    <body>
                        <h2>Compliance test suite for Web Processing Service (WPS) 1.0</h2>
                        <h3>Service metadata</h3>
                        <p>
                            Please provide a URL from which a capabilities document can
                            be retrieved. Modify the URL template below to specify the
                            location of an OGC WPS implementation
                            under test.
                        </p>
                        <blockquote>
                            <table border="1" padding="4" bgcolor="#00ffff">
                                <tr>
                                    <td align="left">Service URL:</td>
                                    <td align="center">
                                        <input name="endpoint-uri" size="128" type="text" value="http://localhost:8080/javaps/rest/"/>
                                    </td>
                                </tr>
                            </table>
                        </blockquote>
                        <h4>Test process</h4>
                        <label>
                        <input type="checkbox" name="test-complex-input" checked="checked" />
                        Test complex input
                        </label>
                        <p>
                            Please provide an process identifier that the execute tests should be run with.
                        </p>
                        <blockquote>
                            <table border="1" padding="4" bgcolor="#00ffff">
                                <tr>
                                    <td align="left">Process identifier:</td>
                                    <td align="center">
                                        <input name="process-identifier" size="128" type="text" value="org.n52.javaps.test.EchoProcess"/>
                                    </td>
                                </tr>
                            </table>
                        </blockquote>
                        <h5>Literal input</h5>
                        <p>
                            Please provide an literal input identifier that the execute tests should be run with.
                        </p>
                        <blockquote>
                            <table border="1" padding="4" bgcolor="#00ffff">
                                <tr>
                                    <td align="left">Literal input identifier:</td>
                                    <td align="center">
                                        <input name="literal-input-identifier" size="128" type="text" value="literalInput"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td align="left">Literal input data type: (optional)</td>
                                    <td align="center">
                                        <input name="literal-input-datatype" size="128" type="text" value=""/>
                                    </td>
                                </tr>
                                <tr>
                                    <td align="left">Literal input unit of measurement: (optional)</td>
                                    <td align="center">
                                        <input name="literal-input-uom" size="128" type="text" value=""/>
                                    </td>
                                </tr>
                                <tr>
                                    <td align="left">Literal input:</td>
                                    <td align="center">
                                        <input name="literal-input" size="128" type="text" value="literal.input"/>
                                    </td>
                                </tr>
                            </table>
                        </blockquote>
                        <h5>Complex input</h5>
                        <p>
                            Please provide an complex input that the execute tests should be run with.
                        </p>
                        <blockquote>
                            <table border="1" padding="4" bgcolor="#00ffff">
                                <tr>
                                    <td align="left">Complex input identifier:</td>
                                    <td align="center">
                                        <input name="complex-input-identifier" size="128" type="text" value="complexInput"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td align="left">Complex input MIME type (optional):</td>
                                    <td align="center">
                                        <input name="complex-input-mimetype" size="128" type="text" value="text/xml"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td align="left">Complex input schema (optional):</td>
                                    <td align="center">
                                        <input name="complex-input-schema" size="128" type="text" value=""/>
                                    </td>
                                </tr>
                                <tr>
                                    <td align="left">Complex input encoding (optional):</td>
                                    <td align="center">
                                        <input name="complex-input-encoding" size="128" type="text" value=""/>
                                    </td>
                                </tr>
                                <tr>
                                    <td align="left">Complex input:</td>
                                    <td align="center">
                                        <textarea name="complex-input-value" style="width:100%"><test>data</test></textarea>
                                    </td>
                                </tr>
                            </table>
                        </blockquote>
                        <h5>Literal output</h5>
                        <p>
                            Please provide an literal output identifier that the execute tests should be run with.
                        </p>
                        <blockquote>
                            <table border="1" padding="4" bgcolor="#00ffff">
                                <tr>
                                    <td align="left">Literal output identifier:</td>
                                    <td align="center">
                                        <input name="literal-output-identifier" size="128" type="text" value="literalOutput"/>
                                    </td>
                                </tr>
                            </table>
                        </blockquote>
                        <h5>Complex output</h5>
                        <p>
                            Please provide an complex output that the execute tests should be run with.
                        </p>
                        <blockquote>
                            <table border="1" padding="4" bgcolor="#00ffff">
                                <tr>
                                    <td align="left">Complex output identifier:</td>
                                    <td align="center">
                                        <input name="complex-output-identifier" size="128" type="text" value="complexOutput"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td align="left">Complex output MIME type (optional):</td>
                                    <td align="center">
                                        <input name="complex-output-mimetype" size="128" type="text" value=""/>
                                    </td>
                                </tr>
                                <tr>
                                    <td align="left">Complex output schema (optional):</td>
                                    <td align="center">
                                        <input name="complex-output-schema" size="128" type="text" value=""/>
                                    </td>
                                </tr>
                                <tr>
                                    <td align="left">Complex output encoding (optional):</td>
                                    <td align="center">
                                        <input name="complex-output-encoding" size="128" type="text" value=""/>
                                    </td>
                                </tr>
                            </table>
                        </blockquote>
                        <input type="submit" value="Start"/>
                    </body>
                </ctl:form>
		</xsl:variable>
        <xsl:variable name="iut-file" select="$form-data//value[@key='doc']/ctl:file-entry/@full-path" />
	      <xsl:variable name="test-run-props">
		    <properties version="1.0">
          <entry key="iut">
            <xsl:choose>
              <xsl:when test="empty($iut-file)">
                <xsl:value-of select="normalize-space($form-data/values/value[@key='endpoint-uri'])"/>
              </xsl:when>
              <xsl:otherwise>
                <xsl:copy-of select="concat('file:///', $iut-file)" />
              </xsl:otherwise>
            </xsl:choose>
          </entry>
          <entry key="ics"><xsl:value-of select="$form-data/values/value[@key='level']"/></entry>
          <entry key="processid"><xsl:value-of select="$form-data/values/value[@key='process-identifier']"/></entry>
          <entry key="testcomplexinput">
             <xsl:variable name="testcomplexinput-checked" select="$form-data/values/value[@key='test-complex-input']" />          
            <xsl:choose>
              <xsl:when test="empty($testcomplexinput-checked)">
                false
              </xsl:when>
              <xsl:otherwise>
                true
              </xsl:otherwise>
            </xsl:choose>
          </entry>
		   <entry key="complexinputvalue"><xsl:value-of select="$form-data/values/value[@key='complex-input-value']"/></entry>
		   <entry key="complexinputid"><xsl:value-of select="$form-data/values/value[@key='complex-input-identifier']"/></entry>
           <entry key="complexinputmimetype"><xsl:value-of select="$form-data/values/value[@key='complex-input-mimetype']"/></entry>
           <entry key="complexinputencoding"><xsl:value-of select="$form-data/values/value[@key='complex-input-encoding']"/></entry>
           <entry key="complexinputschema"><xsl:value-of select="$form-data/values/value[@key='complex-input-schema']"/></entry>
		    </properties>
		   </xsl:variable>
       <xsl:variable name="testRunDir">
         <xsl:value-of select="tec:getTestRunDirectory($te:core)"/>
       </xsl:variable>
       <xsl:variable name="test-results">
        <ctl:call-function name="tns:run-ets-api-processing">
			    <ctl:with-param name="testRunArgs" select="$test-run-props"/>
          <ctl:with-param name="outputDir" select="$testRunDir" />
			  </ctl:call-function>
		  </xsl:variable>
      <xsl:call-template name="tns:testng-report">
        <xsl:with-param name="results" select="$test-results" />
        <xsl:with-param name="outputDir" select="$testRunDir" />
      </xsl:call-template>
      <xsl:variable name="summary-xsl" select="tec:findXMLResource($te:core, '/testng-summary.xsl')" />
      <ctl:message>
        <xsl:value-of select="saxon:transform(saxon:compile-stylesheet($summary-xsl), $test-results)"/>
See detailed test report in the TE_BASE/users/<xsl:value-of 
select="concat(substring-after($testRunDir, 'users/'), '/html/')" /> directory.
        </ctl:message>
        <xsl:if test="xs:integer($test-results/testng-results/@failed) gt 0">
          <xsl:for-each select="$test-results//test-method[@status='FAIL' and not(@is-config='true')]">
            <ctl:message>
Test method <xsl:value-of select="./@name"/>: <xsl:value-of select=".//message"/>
		    </ctl:message>
		  </xsl:for-each>
		  <ctl:fail/>
        </xsl:if>
        <xsl:if test="xs:integer($test-results/testng-results/@skipped) eq xs:integer($test-results/testng-results/@total)">
        <ctl:message>All tests were skipped. One or more preconditions were not satisfied.</ctl:message>
        <xsl:for-each select="$test-results//test-method[@status='FAIL' and @is-config='true']">
          <ctl:message>
            <xsl:value-of select="./@name"/>: <xsl:value-of select=".//message"/>
          </ctl:message>
        </xsl:for-each>
        <ctl:skipped />
      </xsl:if>
	  </ctl:code>
   </ctl:test>

  <xsl:template name="tns:testng-report">
    <xsl:param name="results" />
    <xsl:param name="outputDir" />
    <xsl:variable name="stylesheet" select="tec:findXMLResource($te:core, '/testng-report.xsl')" />
    <xsl:variable name="reporter" select="saxon:compile-stylesheet($stylesheet)" />
    <xsl:variable name="report-params" as="node()*">
      <xsl:element name="testNgXslt.outputDir">
        <xsl:value-of select="concat($outputDir, '/html')" />
      </xsl:element>
    </xsl:variable>
    <xsl:copy-of select="saxon:transform($reporter, $results, $report-params)" />
  </xsl:template>
</ctl:package>
