/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jscover.instrument;

import com.google.debugging.sourcemap.SourceMapParseException;
import com.google.javascript.jscomp.parsing.Config;
import jscover.ConfigurationCommon;
import jscover.instrument.sourcemap.NoOpSourceMap;
import jscover.instrument.sourcemap.SourceMapV3;
import jscover.util.ReflectionUtils;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.BDDMockito.given;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 *
 * @author rrichter
 */
//Function Coverage added by Howard Abrams, CA Technologies (HA-CA) - May 20 2013, tntim96
@RunWith(MockitoJUnitRunner.class)
public class SourceMapInstumenterTest {
	
	 @Mock private ConfigurationCommon config;
    private SourceProcessor sourceProcessor;
    private ParseTreeInstrumenter instrumenter;

    @Before
    public void setUp() throws SourceMapParseException {
        given(config.getECMAVersion()).willReturn(Config.LanguageMode.ECMASCRIPT8);
       // given(config.isIncludeBranch()).willReturn(false);
       // given(config.isIncludeFunction()).willReturn(true);
		
		
		
        sourceProcessor = new SourceProcessor(config, "test.js", "x;", new SourceMapV3("{\n" +
"\"version\":3,\n" +
"\"file\":\"sourcemaptest.min.js\",\n" +
"\"lineCount\":1,\n" +
"\"mappings\":\"AAAAA,MAAAC,sBAAA,CAA+BC,QAAS,CAACC,CAAD,CAAU,CAEjD,IAAIC,EAAUC,SAAAD,QAAA,EAAAE,MAAA,CACL,kBADK,CAAAC,YAAA,CAEC,+BAFD,CAEmCJ,CAAAK,YAFnC,CAEyD,oBAFzD,CAAAC,GAAA,CAGR,IAHQ,CAAAC,OAAA,CAIJ,WAJI,CAKdL,UAAAM,KAAA,CAAeP,CAAf,CAAAQ,KAAA,CAA6B,QAAS,EAAG,CAIxCC,KAAAC,IAAA,CAAUC,WAAAC,SAAAC,gCAAA,EAAV,CAAmE,SAAnE,CAA8Ed,CAA9E,CACE,CAACe,QAAS,CACR,cAAiB,QAAjB,CAA4BH,WAAAI,cAAAC,UAAA,CAAoC,OAApC,CADpB,CAAV,CADF,CAAAR,KAAA,CAKO,QAAS,CAACS,CAAD,CAAW,CACxBrB,MAAAsB,oBAAA,EACAtB,OAAAuB,UAAA,CAAiBpB,CAAjB,CAFwB,CAL3B,CAJwC,CAAzC,CAaG,QAAS,EAAG,EAbf,CAPiD;\",\n" +
"\"sources\":[\"js/test/sourcemaptest.js\"],\n" +
"\"names\":[\"$scope\",\"removeReleaseForStudy\",\"$scope.removeReleaseForStudy\",\"release\",\"confirm\",\"$mdDialog\",\"title\",\"textContent\",\"releaseName\",\"ok\",\"cancel\",\"show\",\"then\",\"$http\",\"put\",\"medigration\",\"security\",\"getStudyReleaseConnectionString\",\"headers\",\"Cookiemanager\",\"getCookie\",\"response\",\"getReleasesForStudy\",\"sendEmail\"]\n" +
"}"));
        instrumenter = (ParseTreeInstrumenter)ReflectionUtils.getField(sourceProcessor, "instrumenter");
    }


    @Test
    public void shouldInstrumentFunctionInTernary() {
        String source = "$scope.removeReleaseForStudy=function(a){var b=$mdDialog.confirm().title(\"Freigabe löschen\").textContent(\"Möchten Sie die Freigabe für \"+a.releaseName+\" wirklich löschen?\").ok(\"Ok\").cancel(\"abbrechen\");$mdDialog.show(b).then(function(){$http.put(medigration.security.getStudyReleaseConnectionString()+\"delete/\",a,{headers:{Authorization:\"Basic \"+medigration.Cookiemanager.getCookie(\"token\")}}).then(function(b){$scope.getReleasesForStudy();$scope.sendEmail(a)})},function(){})};";
        String instrumentedSource = sourceProcessor.instrumentSource(source);
        String expectedSource = "_$jscoverage['js/test/sourcemaptest.js'].lineData[1]++;\n" +
					"$scope.removeReleaseForStudy = function(a) {\n" +
					"  _$jscoverage['js/test/sourcemaptest.js'].lineData[3]++;\n" +
					"  var b = $mdDialog.confirm().title('Freigabe l\\u00f6schen').textContent('M\\u00f6chten Sie die Freigabe f\\u00fcr ' + a.releaseName + ' wirklich l\\u00f6schen?').ok('Ok').cancel('abbrechen');\n" +
					"  _$jscoverage['js/test/sourcemaptest.js'].lineData[8]++;\n" +
					"  $mdDialog.show(b).then(function() {\n" +
					"    _$jscoverage['js/test/sourcemaptest.js'].lineData[12]++;\n" +
					"    $http.put(medigration.security.getStudyReleaseConnectionString() + 'delete/', a, {headers:{Authorization:'Basic ' + medigration.Cookiemanager.getCookie('token')}}).then(function(b) {\n" +
					"      _$jscoverage['js/test/sourcemaptest.js'].lineData[18]++;\n" +
					"      $scope.getReleasesForStudy();\n" +
					"      _$jscoverage['js/test/sourcemaptest.js'].lineData[19]++;\n" +
					"      $scope.sendEmail(a);\n" +
					"    });\n" +
					"  }, function() {\n" +
					"  });\n" +
					"};\n";
        assertEquals(expectedSource, instrumentedSource);
    }
	
}
