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
		<title>DGT Report</title>
		<link href="http://fonts.googleapis.com/css?family=Inconsolata" rel="stylesheet" type="text/css">
		<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">	
		<link href='http://10.207.16.9/DGT/assets/css/dgt.css' type='text/css' rel='stylesheet' />	
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
			    padding-bottom:15px;
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
				<div class='report-name left'>DGT JS Logs Report</div>
				<!-- nav -->
				<nav>			
					<ul id='slide-out' class='right'>
						<li class='analysis waves-effect active'>
							<a href='#!' onclick="_updateCurrentStage(-1)" class='dashboard-view'><i class='mdi-action-track-changes'></i></i> Dashboard</a>
						</li>
						<li class='analysis waves-effect'><a href='#!' class='category-view' onclick="_updateCurrentStage(0)"><i class='mdi-action-language'></i> URLs</a></li>
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
							<span class='total-tests'> <span class='panel-lead'>${dashboard.failedPage+dashboard.passedPage}</span> </span> 
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
								<span class='panel-name'><b>Pages</b></span>
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
								<span class='panel-name'><b>JS Logs</b></span>
							</div> 							 
							<div class='chart-box'>
								<canvas class='text-centered' id='step-analysis'></canvas>
							</div> 
							<div>
								<span class='weight-light'>Errors: <span class='s-pass-count weight-normal'></span>${dashboard.errors} </span>
							</div> 
							<div>							
								<span class='weight-light'>Warnings: <span class='s-fail-count weight-normal'></span>${dashboard.warnings} </span>
							</div> 
						</div> 
					</div>					
					<div class='col s12 m6 l4 fh'> 
						<div class='card-panel'> 
							<span class='panel-name'><b>Pass Percentage</b></span> 
							<div id='percentage-block'>								
								<canvas class="text-centered" id='percentage' data-pass='${dashboard.passedPage}' data-total='${dashboard.passedPage+dashboard.failedPage}'></canvas>
								<span class='pass-percentage panel-lead'></span>
							</div>
							<div class='progress light-blue lighten-3'> 
								<div class='determinate light-blue'></div> 
							</div> 
						</div> 
					</div>		
				</div>
				<div  class='category-summary-view'>
					<div class='col l8 m6 s12 fh'>
							<div class='card-panel'>
								<table class="striped ">
									<thead>
										<tr>
											<th rowspan="2">Category</th>
  											<th rowspan="2">URLs Count</th>
                                          	<th colspan="3" style="text-align:center;">JS Logs Distribution</th>											
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
				<div  class='system-view'>
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
			
			<!-- categories -->			
				<div id='category-view' class='row _addedTable hide'>
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
												
												<div class="table-data hide">	
													<table class="bordered striped">
														<thead>
															<tr>
																<th>Categories</th><th>URL</th><th>Browser</th><th>Logs</th>
															</tr>
														</thead>
														<tbody>
															<#list category.pages as page>
																<tr>																	
																	<td>
																		<#list page.errorCategories as key, value>
																			<span class="status label ${key}" title="${key}">${value}</span>
																		</#list>
																	</td>	
																	<td><a href="${page.url}" target="_blank">${page.url}</a></td>																									
																	<td>${page.browser}</td>
																	<td><a href="#modal1" class='url-info modal-trigger waves-effect waves-light btn' data-url='${page.url}' data-browser='${page.browser}'>View</a></td>
																</tr>
															</#list>
														</tbody>
													</table>	
												</div>
											</li>
										</#list>
									</ul>
								</div>
							</div>
						</div>
					</div>
					<div id='url-details-wrapper' class='col _addedCell2'>						
						<div class="card-panel details-view">
							<div class="urls-header">
								<div class="  ">
									<h6>Category Color Codes:</h6>
									<span class="status label MISCELLANEOUS">MISCELLANEOUS</span>
									<span class="status label ANALYTICS">ANALYTICS</span>
									<span class="status label CQ_LIB">CQ_LIB</span>
									<span class="status label THIRD_PARTY">THIRD_PARTY</span>
									<span class="status label NETWORK">NETWORK</span>
									<span class="status label INTEGRATIONS">INTEGRATIONS</span>
									<span class="status label FONT_FILE">FONT_FILE</span>
									<span class="status label CODE">CODE</span>
									<span class="status label FILE">FILE</span>
									<span class="status label REQUEST">REQUEST</span>
									<br/>
								</div>
							</div>
							<div class="urls-cat-container">
														
							</div>													
						</div>
					</div>
				</div>
			<!-- /categories -->
			
			<footer id='report-footer'>
				Created By <a href='mailto:skumar213@sapient.com'>Sachin Kumar</a>.
			</footer>
		</div>
		<div id='testDataCount' class='hide'>
			<input type='hidden' id='report' name='report' value='${dashboard.reportName}'>
			<input type='hidden' id='passedPage' name='passedPage' value='${dashboard.passedPage}'>
			<input type='hidden' id='failedPage' name='failedPage' value='${dashboard.failedPage}'>
			<input type='hidden' id='errors' name='errors' value='${dashboard.errors}'>
			<input type='hidden' id='warnings' name='warnings' value='${dashboard.warnings}'>
		</div>
		
		<div id='modal1' class='modal modal-fixed-footer'>
		    <div class='modal-content'>
		      <h4>JS Logs</h4>
		      <div id='url'></div>
			  <div id='browser'></div>
		      
		      <hr/>
		      <p></p>
		    </div>
		    <div class='modal-footer'>
		      <a href='#!' class=' modal-action modal-close waves-effect waves-green btn-flat'><strong>Close</strong></a>
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
		
		<script src='http://10.207.16.9/DGT/assets/js/jsTemplate.js' type='text/javascript'></script>
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
