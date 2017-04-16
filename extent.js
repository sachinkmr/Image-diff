/* current view */
var currentView = 0;
var passedPage=$('div#testDataCount input#passedPage').val().replace(/,/g, "");
var failedPage=$('div#testDataCount input#failedPage').val().replace(/,/g, "");
var errors=$('div#testDataCount input#errors').val().replace(/,/g, "");
var warnings=$('div#testDataCount input#warnings').val().replace(/,/g, "");

var data = [{
        value: parseInt(passedPage),
        color: '#00af00',
        highlight: '#32bf32',
        label: 'Pass'
    }, {
        value: parseInt(failedPage),
        color: '#F7464A',
        highlight: '#FF5A5E',
        label: 'Fail'
    }];

var data1 = [{
        value: parseInt(errors),
        color: '#F7464A',
        highlight: '#FF5A5E',
        label: 'Fail'
    },{
        value: parseInt(warnings),
        color: '#FDB45C',
        highlight: '#FFC870',
        label: 'Warning'
    }];

/* fixed-containers */
var ct; // current page id
var chartHeight = 0;

var currentBrowserIE = detectIE();

$(function () {

    ct = $('#test-view');

    var timer = false;
    timer = setInterval(function () {
        _adjustSize();
    }, 200);

    $('._addedTable').mousemove(function () {
        _adjustSize();
    });

    if (currentBrowserIE != false) {
        $('._addedCell1').resizable({
            minWidth: 300,
            handles: "e"
        });
    } else {
        $('._addedCell1').css({
            'resize': 'horizontal'
        })
    }
    _adjustSize();
});


/* -- Check if current page is test or category -- */
function _updateCurrentStage(n) {
    currentView = n;

    if (n === -1) {
        $('body').removeClass('default');
        return;
    }

    $('body').addClass('default');

    window.scrollTo(0, 0);

    chartHeight = 0;

    if (n == 0) {
        ct = $('#category-view');

        setTimeout(function () {
            if ($('.charts').is(':visible'))
                chartHeight = 275;
        }, 200);
    } else if (n == 1)
        ct = $('#categories-view');
    else if (n == 2)
        ct = $('#diff-view');
    else
        return;

    var timer = setTimeout(function () {
        _adjustSize();
        clearTimeout(timer);
    }, 100);
}
/* -- Check if current page is test or category -- */

function _adjustSize() {
    ct.find('._addedTable').css({
        'height': ($(window).height() - 50 - chartHeight) + 'px'
    });

    ct.find('._addedCell1, ._addedCell2').css({
        'height': ($(window).height() - 50 - chartHeight) + 'px'
    });
    ct.find('._addedCell1 .contents, ._addedCell2 .contents').css({
        'height': ($(window).height() - 65 - chartHeight) + 'px'
    });

    if ($(window).width() < 992)
        ct.find('._addedCell2').css({
            'width': Math.round($(window).width() - 5 - ct.find('._addedCell1').width()) + 'px'
        });
    else
        ct.find('._addedCell2').css({
            'width': Math.round($(window).width() - 5 - ct.find('._addedCell1').width()) + 'px'
        });

}


function detectIE() {
    var ua = window.navigator.userAgent;

    var msie = ua.indexOf('MSIE ');
    if (msie > 0) {
        // IE 10 or older => return version number
        return parseInt(ua.substring(msie + 5, ua.indexOf('.', msie)), 10);
    }

    var trident = ua.indexOf('Trident/');
    if (trident > 0) {
        // IE 11 => return version number
        var rv = ua.indexOf('rv:');
        return parseInt(ua.substring(rv + 3, ua.indexOf('.', rv)), 10);
    }

    var edge = ua.indexOf('Edge/');
    if (edge > 0) {
        // Edge (IE 12+) => return version number
        return parseInt(ua.substring(edge + 5, ua.indexOf('.', edge)), 10);
    }

    // other browser
    return false;
}



/* side-nav navigation [SIDE-NAV] */
$('.analysis').click(function () {
    $('body').addClass('hide-overflow');
    $('.container > .row').addClass('hide');

    var el = $(this);
    var cls = el.children('a').prop('class');

    $('#' + cls).removeClass('hide');

    if (cls == 'test-view') {
        if ($('#enableDashboard').hasClass('enabled') && $('#dashboard-view').hasClass('hide'))
            $('#enableDashboard').click().addClass('enabled');
    } else {
        if (cls == 'dashboard-view' || cls == 'testrunner-logs-view')
            $('body').removeClass('hide-overflow');

        // if any other view besides test-view, show all divs of dashboard-view
        $('#dashboard-view > div').removeClass('hide');

    }

    $('#slide-out > .analysis').removeClass('active');
    el.addClass('active');
});






$('.exception-item').click(function () {	
    $('#url-collection .exception-item').removeClass('active');
    var el = $(this).addClass('active');
	$('#url-details-wrapper').html('');
	var data=$(this).find('.details-view').clone();
	$('#url-details-wrapper').html(data);
	$('#url-details-wrapper .details-view').removeClass('hide');	
});






/* move up and down to browse tests */
$(window).keydown(function (e) {
    var target = null,
            sibling = null;

    (currentView === 0) && (target = $('li.test.displayed.active'), sibling = '.test.displayed');
    (currentView === 1) && (target = $('li.category-item.displayed.active'), sibling = '.category-item.displayed');
    (currentView === 2) && (target = $('li.exception-item.displayed.active'), sibling = '.exception-item.displayed');

    if (target !== null) {
        (e.which === 40) && target.nextAll(sibling).first().click();
        (e.which === 38) && target.prevAll(sibling).first().click();
    }
});


/* formats date in mm-dd-yyyy hh:mm:ss [UTIL] */
function formatDt(d) {
    return d.getFullYear() + '-' + ('00' + (d.getMonth() + 1)).slice(-2) + '-' + ('00' + d.getDate()).slice(-2) + ' ' + ('00' + d.getHours()).slice(-2) + ':' + ('00' + d.getMinutes()).slice(-2) + ':' + ('00' + d.getSeconds()).slice(-2);
}


/* dashboard chart options [DASHBOARD] */
var options = {
    segmentShowStroke: false,
    percentageInnerCutout: 55,
    animationSteps: 100,
    // String - Animation easing effect
    animationEasing: "easeOutBounce",
    // Boolean - Whether we animate the rotation of the Doughnut
    animateRotate: true,
    // Boolean - Whether we animate scaling the Doughnut from the centre
    animateScale: true,
    legendTemplate: '<ul class=\'<%=name.toLowerCase()%>-legend\'><% for (var i=0; i<segments.length; i++) {%><li><%if(segments[i].label && segments[i].value){%><span style=\'background-color:<%=segments[i].fillColor%>\'></span><%=segments[i].label%><%}%></li><%}%></ul>'
};



/* draw legend for test and step charts [DASHBOARD] */
function drawLegend(chart, id) {
    var helpers = Chart.helpers;
    var legendHolder = document.getElementById(id);
    legendHolder.innerHTML = chart.generateLegend();

    helpers.each(legendHolder.firstChild.childNodes, function (legendNode, index) {
        helpers.addEvent(legendNode, 'mouseover', function () {
            var activeSegment = chart.segments[index];
            activeSegment.save();
            activeSegment.fillColor = activeSegment.highlightColor;
            chart.showTooltip([activeSegment]);
            activeSegment.restore();
        });
    });
    Chart.helpers.addEvent(legendHolder.firstChild, 'mouseout', function () {
        chart.draw();
    });
    $('#' + id).after(legendHolder.firstChild);
}


function fetchURLResults() {
	$('.details-container #loadMore').html('<i class="material-icons left">loop</i> Loading Results...');	
	$('.details-container #loadMore').attr('data-clickable', 'false');
	$('.details-container #loadMore').removeClass('hide');
	if(totalLogs<limit){
		$('.details-container #loadMore').addClass('hide');		
		return;
	}
	//var url='http://10.207.60.191:'+port+'/JSON_validator/'+$('#testDataCount #report').val()+'/?filter_test_name='+testName+'&limit='+limit+'&skip='+page;
	var url='http://10.207.16.9/JSON-Validator/FetchResults?report='+$('#testDataCount #report').val()+'&test_name='+testName+'&limit='+limit+'&skip='+page;
	
    $.ajax({
        url: url,
        type: 'get',		
		dataType: 'jsonp',
		crossDomain: true,
		jsonp: 'jsonp', 		
		error: function (XMLHttpRequest, textStatus, errorThrown) {
			console.error( errorThrown);
		},
        success: function (result) {
			result=JSON.stringify(result);
			result=result.substring(result.indexOf('({')+1,result.length);	
			result=$.parseJSON(result);
			totalLogs=result.total_rows;
            page = page + limit;            
			if(totalLogs<limit){
				$('.details-container #loadMore').addClass('hide');
			}
			$('.details-container #loadMore').attr('data-clickable', 'true');
			$('.details-container #loadMore').html('Load More Results');
            $.each(result.rows, function (index, log) {   
			//	log=$.parseJSON(log); <td><div class='status label capitalize "+log.status.toLowerCase()+"'>"+log.status+"</div></td>
                $('.details-container .test-body .test-steps table.table-results tbody').append('<tr></tr>');
                var ic = "<td class='status " + log.status.toLowerCase() + "' title='" + log.status + "' alt='" + log.status + "'><div class='status label capitalize "+log.status.toLowerCase()+"'>"+log.status+"</div></td><td class='timestamp'>" + log.time + "</td><td class='step-name'>" + log.step + "</td><td class='step-details'>" + log.detail + "</td>";
                $('.details-container .test-body .test-steps table.table-results >tbody >tr:last-child').html(ic);	
            });  
			initDetails();			
        }
    });
}

function initDetails(){	
	$('.modal-trigger').leanModal();
}


$(document).ready(function () {
    /* init */
    $('select').material_select();
    /* select the first category item in categories view by default */
    $('.exception-item').eq(0).click();	
    $('.details-container .test-body .test-steps table.table-results').css('display', 'table');
	/*----- Charts   ----*/
	var ctx2 = $('#test-analysis').get(0).getContext('2d');
	var testChart = new Chart(ctx2).Doughnut(data, options);
	drawLegend(testChart, 'test-analysis');
	var ctx1 = $('#step-analysis').get(0).getContext('2d');
	var stepChart = new Chart(ctx1).Doughnut(data1, options);
	drawLegend(stepChart, 'step-analysis');
	$('li.analysis.waves-effect.active').click();		
	var passed = $('canvas#percentage').attr('data-pass').replace(/,/g, "");
	var total = $('canvas#percentage').attr('data-total').replace(/,/g, "");
	var percentage = Math.round((passed * 100) / (total));
	var pieData = [{
        value: percentage,
        color: "#3F9F3F"
    },
    {
        value: 100 - percentage,
        color: "#eceff5"
    }];
	var ctx = $('#percentage').get(0).getContext('2d');
	var stepChart1 = new Chart(ctx).Doughnut(pieData, options);
	drawLegend(stepChart1, 'percentage');
	$('ul.doughnut-legend').addClass('right');
	$('.pass-percentage.panel-lead').text(percentage + '%');
	$('#dashboard-view .determinate').attr('style', 'width:' + percentage + '%');
	
	/*----- Charts Ends  ----*/
});


// Category page switch
$('.category-summary-view .pageCates').click(function(){		
	var label=$(this).text();
	$('.exception-item .url-name').filter(function(){
		return ($(this).text()==label);
	}).click();
	$('.analysis >.category-view').click();
});
