<#assign dateFormat = "dd-MMMM-YYYY">
<#assign timeFormat = "h:mm:ss a">
<#assign dateTimeFormat = "dd-MMMM-YYYY" + " " + "h:mm:ss a">

<!DOCTYPE html>
<html>
	<head>		
		<meta name='editor' content='Sachin Kumar'>
		<meta charset='UTF-8' /> 
		<meta name='description' content='' />
		<meta name='robots' content='noindex, noodp, noydir' />
		<meta name='viewport' content='width=device-width, initial-scale=1' />
		<title>JSON Validator Report</title>
		<link href="http://fonts.googleapis.com/css?family=Inconsolata" rel="stylesheet" type="text/css">
		<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">	
		<link href='https://cdn.rawgit.com/sachinkmr/Content/603c015f7c5df430482f89ab9e638beb34ffcfd2/SEOBOX/css/extent.css' type='text/css' rel='stylesheet' />	
		<style type="text/css">
			.category-summary-view thead th{
		  		border: 1px solid #ddd !important;
		    }
			html,body {
				text-rendering: optimizeLegibility !important;
				-webkit-font-smoothing: antialiased !important;
			}
			html, html a {
			    text-shadow: 1px 1px 1px rgba(0,0,0,0.004);
			}
			div.card-panel div#percentage-block{
				position: relative;
			}
			
			div.card-panel div#percentage-block span.pass-percentage.panel-lead{
				position: absolute;
    			top: 30%;
    			left: 55%;
			}
			
			.container {
    			padding-left: 0px; 
			}
			.report-info{
				display:block;
			}
			div.report-info {
				width: 100%;
			}
			header {
			    height: auto;
			}
			#test-collection, #cat-collection ,#url-collection{
			    margin: 0;
			    margin-bottom: 50px;
			}
			.loading {
			   font-size:xx-large;
			    position: fixed;
			    width: 100%;
			    height: 100%;
			    background: black;
			    opacity: .78; 
			    display:none;
			    z-index:1000000;
			    color:white;
			    
			}
			.loading>#error {
				margin: 100px 225px;
			    padding: 0 0 17px 0;
			    border-bottom: white dotted;
			}
			td,tr{
				border-bottom:0;
			}
			.categories{
				line-height: 1.8em;
			}
			#url-details-wrapper .pageUrl{
				width: 100%;	
				margin-bottom:5px;
			}
			#url-details-wrapper .urls-header{
				display: inline-block;
			    border-bottom: 1px solid #ddd;
			    width: 100%;		
			}
			.urls-cat-container{
				padding-top:10px;
			}
			.indexInfo{
				width:100%;
			}
		</style>
	</head>	
	<body class='extent default hide-overflow' onload="_updateCurrentStage(-1)">
	<div class="loading style-2 error">
	    <div id='error'></div>
	</div>
		<header>			
			<div class="blue darken-2 report-info right">
				<div class='report-name left'>Difference Generation Tool</div>
				<!-- nav -->
				<nav>			
					<ul id='slide-out' class='right'>
						<li class='analysis waves-effect active'>
							<a href='#!' onclick="_updateCurrentStage(-1)" class='dashboard-view'><i class='mdi-action-track-changes'></i></i> Dashboard</a>
						</li>
						<li class='analysis waves-effect'><a href='#!' class='category-view' onclick="_updateCurrentStage(0)"><i class='mdi-action-language'></i> Categories</a></li>
					<#if dashboard.diff=="yes">
						<li class='analysis waves-effect'><a href='#!' class='diff-view' onclick="_updateCurrentStage(1)"><i class='mdi-action-language'></i> Difference</a></li>
					</#if>
					</ul>			
				</nav>
		<!-- /nav -->		
			</div>						
		</header>
		
		<!-- container -->
		<div class='container'>
			
			<!-- dashboard -->
			<div id='dashboard-view' class='row'>
				<div class='time-totals'>
					<div class='col l2 m4 s6'>
						<div class='card suite-total-tests'> 
							<span class='panel-name'><b>Total Pages</b></span> 
							<span class='total-tests'> <span class='panel-lead'>${dashboard.totalPage}</span> </span> 
						</div> 
					</div>
					<div class='col l2 m4 s6'>
						<div class='card suite-total-steps'> 
							<span class='panel-name'><b>Total JS Logs</b></span> 
							<span class='total-steps'> <span class='panel-lead'>${dashboard.totalCount}</span> </span> 
						</div> 
					</div>
					<div class='col l2 m4 s12'>
						<div class='card suite-total-time-current'> 
							<span class='panel-name'><b>Suite Execution Time</b></span> 
							<span class='suite-total-time-current-value panel-lead'>${dashboard.runDuration}</span> 
						</div> 
					</div>
					<div class='col l2 m4 s12'>
						<div class='card suite-total-time-overall'> 
							<span class='panel-name'><b>Total Time Taken (Overall)</b></span> 
							<span class='suite-total-time-overall-value panel-lead'>${dashboard.runDurationOverall}</span> 
						</div> 
					</div>
					<div class='col l2 m4 s6 suite-start-time'>
						<div class='card accent green-accent'> 
							<span class='panel-name'><b>Report Start Time:</b></span> 
							<span class='panel-lead suite-started-time'>${dashboard.startedTime?datetime?string(dateTimeFormat)}</span> 
						</div> 
					</div>
					<div class='col l2 m4 s6 suite-end-time'>
						<div class='card accent pink-accent'> 
							<span class='panel-name'><b>Report End Time:</b></span> 
							<span class='panel-lead suite-ended-time'>${.now?datetime?string(dateTimeFormat)}</span> 
						</div> 
					</div>
				</div>
				<div class='charts'>
					<div class='col s12 m6 l4 fh'> 
						<div class='card-panel'> 
							<div>
								<span class='panel-name'><b>Page Urls View</b></span>
							</div> 							
							<div class='chart-box'>
								<canvas class='text-centered' id='test-analysis'></canvas>
							</div> 
							<div>
								<span class='weight-light'>Passed: <span class='t-pass-count weight-normal'>${dashboard.passedPage}</span> Page(s)</span>
							</div> 
							<div>
								<span class='weight-light'>Failed: <span class='t-fail-count weight-normal'>${dashboard.failedPage}</span> Page(s)</span>
							</div> 
						</div> 
					</div> 
					<div class='col s12 m6 l4 fh'> 
						<div class='card-panel'> 
							<div>
								<span class='panel-name'><b>JS Logs View</b></span>
							</div> 							 
							<div class='chart-box'>
								<canvas class='text-centered' id='step-analysis'></canvas>
							</div> 
							<div>
								<span class='weight-light'>Errors: <span class='s-pass-count weight-normal'></span>${dashboard.errors} </span>
							</div> 
							<div>							
								<span class='weight-light'>Warnings: <span class='s-fail-count weight-normal'></span>${dashboard.warnings} component(s)</span>
							</div> 
						</div> 
					</div>
					<div class='col s12 m12 l4 fh'> 
						<div class='card-panel'> 
							<span class='panel-name'><b>Pass Percentage</b></span> 
							<div id='percentage-block'>								
								<canvas class="text-centered" id='percentage'></canvas>
								<span class='pass-percentage panel-lead'></span>
							</div>
							<div class='progress light-blue lighten-3'> 
								<div class='determinate light-blue'></div> 
							</div> 
						</div> 
					</div>
				</div>				
					<div class='category-summary-view'>
						<div class='col l8 m6 s12'>
							<div class='card-panel'>
								<table class="striped ">
									<thead>
										<tr>
											<th rowspan="2">Category</th>
  											<th rowspan="2">URLs Count</th>
                                          <th colspan="3">JS Logs Distribution</th>											
										</tr>
										<tr>
											<th>Errors</th>
											<th>Warnings</th>
											<th>Total</th>
 									 	</tr>									
									</thead>
									<tbody>
										<#list dashboard.categories?sort as category>
											<tr data-name='${category.name}'>
												<td>
												<a href='#' class='pageCates'>${category.name}</a>
												</td>	
												<td id='urlCount'>
													${category.pages?size}	
												</td>											
												<td>
													${category.errorCount}
												</td>
												<td>													
													${category.warningCount}
												</td>
												<td>
													${category.warningCount+category.errorCount}
												</td>
											</tr>
										</#list>
									</tbody>
								</table>
							</div>
						</div>
					</div>	
				<div class='system-view'>
					<div class='col l4 m12 s12'>
						<div class='card-panel'>
							<span class='label info outline right'><b>Environment</b></span>
							<table class="striped">
								<thead>
									<tr>
										<th>PARAM</th>
										<th>VALUE</th>
									</tr>
								</thead>
								<tbody>
								<#list dashboard.systemInfo as key, value>
										<tr>
											<td>${key}</td>
											<td>${value}</td>
										</tr>
									</#list>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				
			</div>
			<!-- /dashboard -->
			
			<!-- tests -->
			<div id='test-view' class='row _addedTable'>
				<div class='col _addedCell1'>
					<div class='contents'>
						<div class='card-panel heading'>
							<h5>Components</h5>
						</div>
						<div class='card-panel filters'>
							<div>
								<a class='dropdown-button btn-floating btn-small waves-effect waves-light grey tests-toggle' data-activates='tests-toggle' data-constrainwidth='true' data-beloworigin='true' data-hover='true' href='#'>
									<i class='mdi-action-reorder'></i>
								</a>
								<ul id='tests-toggle' class='dropdown-content'>
									<li class='pass'><a href='#!'>Pass</a></li>
									<li class='fail'><a href='#!'>Fail</a></li>
									<#if dashboard.logStatusList?? && dashboard.logStatusList?seq_contains(LogStatus.FATAL)>
										<li class='fatal'><a href='#!'>Fatal</a></li>
									</#if>
									<#if dashboard.logStatusList?? && dashboard.logStatusList?seq_contains(LogStatus.ERROR)>
										<li class='error'><a href='#!'>Error</a></li>
									</#if>
									<#if dashboard.logStatusList?? && dashboard.logStatusList?seq_contains(LogStatus.WARNING)>
										<li class='warning'><a href='#!'>Warning</a></li>
									</#if>	
									<li class='skip'><a href='#!'>Skip</a></li>
									<#if dashboard.logStatusList?? && dashboard.logStatusList?seq_contains(LogStatus.UNKNOWN)>
										<li class='unknown'><a href='#!'>Unknown</a></li>
									</#if>	
									<li class='divider'></li>
									<li class='clear'><a href='#!'>Clear Filters</a></li>
								</ul>
							</div>
								<div>
									<a class='dropdown-button btn-floating btn-small waves-effect waves-light grey category-toggle' data-activates='category-toggle' data-constrainwidth='false' data-beloworigin='true' data-hover='true' href='#'>
										<i class='mdi-maps-local-offer'></i>
									</a>
									<ul id='category-toggle' class='dropdown-content'>
										<#list dashboard.dashBoardCategories?sort as category>
											<li class='${category.name}'><a href='#!'>${category.name}</a></li>
										</#list>
										<li class='divider'></li>
										<li class='clear'><a href='#!'>Clear Filters</a></li>
									</ul>
								</div>
							<div>
								<a class='btn-floating btn-small waves-effect waves-light grey' id='clear-filters' alt='Clear Filters' title='Clear Filters'>
									<i class='mdi-navigation-close'></i>
								</a>
							</div>
							
							<div class='search' alt='Search Tests' title='Search Tests'>
								<div class='input-field left'>
									<input id='searchTests' type='text' class='validate' placeholder='Search Tests...'>
								</div>
								<a href="#" class='btn-floating btn-small waves-effect waves-light grey'>
									<i class='mdi-action-search'></i>
								</a>
							</div>
						</div>
						<div class='card-panel no-padding-h no-padding-v no-margin-v'>
							<div class='wrapper'>
								<ul id='test-collection' class='test-collection'>
									<#list dashboard.testCases?sort as test>										
										<li class='collection-item test displayed active ${test.status}' extentid='${test.id?string}'>
											<div class='test-head'>
												<span class='test-name'>${test.name}</span>
												<span class='test-status label right outline capitalize ${test.status}'>${test.status}</span>
												<span class='category-assigned hide <#list test.cats as category> ${category?lower_case?replace(".", "")?replace("#", "")?replace(" ", "")}</#list>'></span>
											</div>
											<div class='test-body'>
												<div class='test-info'>
													<div class='test-info-pane1'>
														<div title='Test started time' alt='Test started time' class='test-started-time'><b>Start Time: </b>${test.time?datetime?string(dateTimeFormat)}</div>
														<div title='Test ended time' alt='Test ended time' class='test-ended-time'><b>End Time: </b><#if test.endedTime??>${test.endedTime?datetime?string(dateTimeFormat)}</#if></div>
														<div title='Time taken to finish' alt='Time taken to finish' class='test-time-taken'><b>Execution Time: </b><#if test.endedTime??>${test.getRunDuration()}</#if></div>
														
														<div class='test-attributes'>
															<#if test.cats?? && test.cats?size != 0>
																<div class='categories'>
																<b>Content Type: </b>
																	<#list test.cats as category>
																		<span class='category text-white'>${category}</span>
																	</#list>
																</div>
															</#if>
														</div>
														<div class='indexInfo'><b>Description: </b>In details column for table below, following are terminologies:<br/> - Index value is based on element index calculated by CSS query "div.component-wrapper> div.component" <br/> - Position is calculated based on value of the attribute "data-component-positions".</div>
													</div>
												</div>												
												<div class='test-steps'>
													<table class='bordered table-results striped'>
														<thead>
															<tr>
																<th>Status</th>
																<th>TimeStamp</th>
																<th>StepInfo</th>																
																<th>Details</th>
															</tr>
														</thead>
														<tbody>													
														</tbody>
													</table>	
													<button id="loadMore" class="waves-effect waves-light btn hide loadMore" style="margin:20px auto;" data-clickable="false">Load More Results</button>
												</div>
											</div>
										</li>
									</#list>
								</ul>
							</div>
						</div>
					</div>
				</div>
				<div id='test-details-wrapper' class='col _addedCell2'>
					<div class='contents'>
						<div class='card-panel details-view'>
							<h5 class='details-name'></h5>
							<div class='step-filters right'>
								<span class='btn-floating btn-small waves-effect waves-light blue' status='info' alt='info' title='info'><i class='mdi-action-info-outline'></i></span>
								<span class='btn-floating btn-small waves-effect waves-light green' status='pass' alt='pass' title='pass'><i class='mdi-action-check-circle'></i></span>
								<span class='btn-floating btn-small waves-effect waves-light red' status='fail' alt='fail' title='fail'><i class='mdi-navigation-cancel'></i></span>
								<span class='btn-floating btn-small waves-effect waves-light red darken-4' status='fatal' alt='fatal' title='fatal'><i class='mdi-navigation-cancel'></i></span>
								<span class='btn-floating btn-small waves-effect waves-light red lighten-2' status='error' alt='error' title='error'><i class='mdi-alert-error'></i></span>
								<span class='btn-floating btn-small waves-effect waves-light orange' alt='warning' status='warning' title='warning'><i class='mdi-alert-warning'></i></span>
								<span class='btn-floating btn-small waves-effect waves-light cyan' status='skip' alt='skip' title='skip'><i class='mdi-content-redo'></i></span>
								<span class='btn-floating btn-small waves-effect waves-light grey darken-2' status='clear-step-filter' alt='Clear filters' title='Clear filters'><i class='mdi-content-clear'></i></span>
							</div>
							<div class='details-container'>
							</div>
						</div>
					</div>
				</div>
			</div>
			<!-- /tests -->
			
			<!-- categories -->			
				<div id='categories-view' class='row _addedTable hide'>
					<div class='col _addedCell1'>
						<div class='contents'>
							<div class='card-panel heading'>
								<h5>Content Type</h5>
							</div>
							<div class='card-panel filters'>
								<div class='search' alt='Search tests' title='Search tests'>
									<div class='input-field left'>
										<input id='searchTests' type='text' class='validate' placeholder='Search...'>
									</div>
									<a href="#" class='btn-floating btn-small waves-effect waves-light blue lighten-1'>
										<i class='mdi-action-search'></i>
									</a>
								</div>
							</div>
							<div class='card-panel no-padding-h no-padding-v'>
								<div class='wrapper'>
									<ul id='cat-collection' class='cat-collection'>
										<#list dashboard.dashBoardCategories?sort as category>	
											<#assign others = category.total-(category.passed+category.failed)>
											<li class='category-item displayed'>
												<div class='cat-head'>
													<span class='category-name'>${category.name}</span>
												</div>
												<div class='category-status-counts'>
													<#if (category.passed > 0)>
														<span class='pass label dot'>Pass: ${category.passed}</span>
													</#if>
													<#if (category.failed > 0)>
														<span class='fail label dot'>Fail: ${category.failed}</span>
													</#if>
													<#if (others > 0)>
														<span class='other label dot'>Others: ${others}</span>
													</#if>
												</div>
												<div class='cat-body'>
													<div class='category-status-counts'>
														<div class='button-group'>
															<a href='#!' class='pass label filter'>Pass <span class='icon'>${category.passed}</span></a>
															<a href='#!' class='fail label filter'>Fail <span class='icon'>${category.failed}</span></a>
															<a href='#!' class='other label filter'>Others <span class='icon'>${others}</span></a>
														</div>
													</div>
													<div class='cat-tests'>
														<table class='bordered striped'>
															<thead>
																<tr>
																	<th>RunDate</th>
																	<th>Test Name</th>
																	<th>Status</th>
																</tr>
															</thead>
															<tbody>
																<#list category.testCases?sort as test>
																	<tr class='${test.status}'>
																		<td>${test.time?datetime?string(dateTimeFormat)}</td>
																		<td><span class='category-link linked' extentid='${test.id?string}'>${test.name}</span></td>
																		<td><div class='status label capitalize ${test.status}'>${test.status}</div></td>
																	</tr>
																</#list>
															<tbody>
														</table>
													</div>
												</div> 
											</li>
										</#list>
									</ul>
								</div>
							</div>
						</div>
					</div>
					<div id='cat-details-wrapper' class='col _addedCell2'>
						<div class='contents'>
							<div class='card-panel details-view'>
								<h5 class='cat-name'></h5>
								<div class='cat-container'>
								</div>
							</div>
						</div>
					</div>
				</div>
			<!-- /categories -->
			<!-- categories -->			
				<div id='urls-view' class='row _addedTable hide'>
					<div class='col _addedCell1'>
						<div class='contents'>
							<div class='card-panel heading'>
								<h5>JS Categories</h5>
							</div>			
							<div class='card-panel no-padding-h no-padding-v'>
								<div class='wrapper'>
									<ul id='url-collection' class='url-collection exception-collection'>
										<#list dashboard.categories?sort as category>
											<li class='exception-item displayed'>
												<div class='url-head exception-head'>
													<span class='url-name'>${category.name}</span>
												</div>
												<div class="card-panel details-view hide">
													<div class="urls-header">
														<h5 class="urls-cat-name left">${category.name}</h5>
														<div class="url-close btn waves-effect text-darken-2 waves-light right hide">Close</div>
													</div>
													<div class="urls-cat-container">
														<table class="bordered striped">
															<thead>
																<tr>
																	<th>Details</th>	
																	<th>URL</th>																									
																	<th>Browser</th>
																</tr>
															</thead>
															<tbody>
															<#list category.pages as page>
																<tr>
																	<td><span class="status label fail" title="Errors">${page.errorCount}</span> <span class="status label warn" title="Warning">${page.warningCount}</span></td>
																	<td>${page.url}</td>																									
																	<td>${page.browser}</td>
																</tr>
															</#list>	
															</tbody>
														</table>								
													</div>													
												</div>	
											</li>
										</#list>
									</ul>
								</div>
							</div>
						</div>
					</div>
					<div id='url-details-wrapper' class='col _addedCell2'>
						
					</div>
				</div>
			<!-- /categories -->
			
			<footer id='report-footer'>
				Created By <a href='mailto:skumar213@sapient.com'>Sachin Kumar</a>.
			</footer>
		</div>
		<div id='testDataCount' class='hide'>
			<input type='hidden' id='report' name='report' value='${dashboard.reportName}'>
		</div>
		
		<div id='modal1' class='modal modal-fixed-footer'>
		    <div class='modal-content'>
		      <h4>Errors in Component</h4>
		      <p></p>
		    </div>
		    <div class='modal-footer'>
		      <a href='#!' class=' modal-action modal-close waves-effect waves-green btn-flat'><strong>Close</strong></a>
		    </div>
		</div>
		
		<div id='URLContent' class='URLContent hide'>
			<div class='contents'>
				<div class='card-panel details-view'>
					<div class='urls-header'>
						<h5 class='urls-cat-name left'></h5>
						<div class="url-close btn waves-effect text-darken-2 waves-light right hide">Close</div>
					</div>
					<div class='urls-cat-container'>
						<table class='bordered striped'>
							<thead>
								<tr>
									<th>Status</th>
									<th>URL</th>																									
									<th>Details</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
						<button id="loadMoreURLs" class="waves-effect waves-light btn hide loadMore" style="margin:20px auto;" data-clickable="false">Load More URLs</button>
					</div>
					<div class='urlDetailDiv hide'>
						<div class='pageUrl'><b>URL: </b><span class='data-url'></span> </div>
						<div class='indexInfo'><b>Description: </b>In details column for table below, following are terminologies:<br/> - Index value is based on element index calculated by CSS query "div.component-wrapper> div.component" <br/> - Position is calculated based on value of the attribute "data-component-positions".<p>&nbsp;</p></div>
						<table class='bordered striped'>
							<thead>
								<tr>
									<th>Status</th>
									<th>TimeStamp</th>
									<th>StepInfo</th>																
									<th>Details</th>
								</tr>
							</thead>
							<tbody>													
							</tbody>
						</table>	
					</div>
				</div>
			</div>
		</div>
		<!--
		<script src='https://cdn.rawgit.com/sachinkmr/Content/8a2bed806fece1a3d4ca39f491ba23641b213261/JsonValidator/js/extent-jsonp.js' type='text/javascript'></script>
		<script src='https://cdn.rawgit.com/anshooarora/extentreports/ab0f4299b133bfa234cec0b1e0ac08a692a7640a/cdn/extent.js' type='text/javascript'></script>
		-->
		<script   src="https://code.jquery.com/jquery-2.2.0.min.js"   integrity="sha256-ihAoc6M/JPfrIiIeayPE9xjin4UWjsx2mjW/rtmxLM4="   crossorigin="anonymous"></script>
		<script   src="https://code.jquery.com/ui/1.11.4/jquery-ui.min.js"   integrity="sha256-xNjb53/rY+WmG+4L6tTl9m6PpqknWZvRt0rO1SRnJzw="   crossorigin="anonymous"></script>		
		<script src='https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.5/js/materialize.min.js' type='text/javascript'></script>
		<script src='https://cdnjs.cloudflare.com/ajax/libs/Chart.js/1.0.1/Chart.min.js' type='text/javascript'></script>
		<script src='https://cdnjs.cloudflare.com/ajax/libs/featherlight/1.3.4/featherlight.min.js' type='text/javascript'></script>		
		
		<script src='https://cdn.rawgit.com/sachinkmr/Content/6fb00fae84b16867c6412dfc0197547f8973a861/JsonValidator/js/extent.js' type='text/javascript'></script>
		<script>		
			if($('.system-view>div>div.card-panel').css('height')>$('.category-summary-view>div>div.card-panel').css('height')){
				$('.category-summary-view>div >div.card-panel').css('height',$('.system-view>div> div.card-panel').css('height'));
			}
			$(document).ready(function() {
			  $('.modal-trigger').leanModal();
			});
			
			
		</script>
	</body>
</html>