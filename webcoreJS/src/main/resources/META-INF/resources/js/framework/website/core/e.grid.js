/**
 * File：e.grid.js
 * Function：基于datatables插件进行二次封装的处理
 * Author：Terry
 * Create：2012.11.27 
 * 
 * 依赖功能脚本：
 * e.core.js(eAjax,copyProperties)
 * colResizable.js(列拖拽，目前不可用)
 * 
 * Change Log:
 * 2014.12.30
 * 修复gridReload方法中不会使用当前参数重载表格的问题
 * 新增保存表格数据状态功能
 * 2014.12.31
 * 完善分页信息栏显示内容，增加当前页，总页数显示
 * 2016.01.12
 * 增加设置“显示/隐藏列”的按钮，来控制显示列
 * 2016.01
 * 修改dataTable的数据读取方法从原旧的方式修改为新的ajax方式，控制更灵活
 * 2016.02.18
 * 解决打开水平/垂直滚动条后，自定义的选择列会多出一行的问题
 * 增加判断有效显示列10个以上才自动打开滚动条
 * 2016.02.19
 * 解决打开水平/垂直滚动条后，标题栏宽度与数据表格宽度会相差1PX的问题
 * 2016.03.14
 * 增加设置全部列的对齐方式
 * 2016.06.29
 * 修改脚本及样式解决表格的选择控件（radio,checkbox）居中对齐问题
 * 2016.06.30
 * 解决默认排序列总是为第一列问题，它会根据最左一个打开排序列作为默认排序列
 * 增加若所有列都关闭排序功能，则直接关闭表格的排序功能
 * 增加指定列作为初始化列功能（pDefaultOrderColumn），但要求该列同样是打开排序功能，否则设置无效
 * 增加行选中回调事件（eRowSelect）
 * 2016.07.22
 * 解决多选模式下，点击checkbox自身没有触发eClick事件
 * 2016.11.07
 * 增加表格的行内编辑模式，目前仅支持编辑功能，新增功能未完成
 * 增编辑模式列初始化函数editInit
 * 增加rowEdit函数，启动行编辑模式，并触发所有配置了editInit属性的列
 * 增加getRowForm，获得行表单内容
 * 增加rowEditDone完成编辑函数
 * 2017.03.27
 * 修改事件绑定的方式
 * 解决事件重复绑定的问题
 * 2017.04.12
 * 解决在IE下checkbox双击时不会改变选中值的问题，视觉上像是点击反应滞后
 * 2017.04.13
 * 解决列模型升级程序中的空处理
 * 2017.04.17
 * 去除原有将表格对象进行缓存的功能
 * 2017.06.08
 * 解决eClick设置无效的问题
 */
;(function($) {
	"use strict"; // 使用严格模式Javascript
	//默认参数集
	var _defaults = {
		/**
		 * 自定义属性集
		 */
		'pCheck' : 'single',                     //false:不允许选中行 single:单行选中 multi:多行选中
		'pCheckColumn' : true,                  //是否显示单选/复选框的列
		'pCheckRow' : true,                     //是否允许点击行任意位置使得行被选中
		'pSingleCheckClean' : false,            //单选模式下，选中行后，再次点击行是否选中行true：清除选中false:不清除选中
		'pColDrag' : false,                     //列是否允许被移动
		'pColResize' : false,                   //列是否允许被手动修改宽度，已可用，但样式会有些问题
		'pColumnVis' : true,                    //是否增加按钮，以让客户可以自由设置页面显示的列
		'pColAlign' : false,                    //设置表格的全部列的排序方式[dt-left左,dt-center中,dt-right右]，默认为向左对齐
		
		'pTableTools' : false,                  //表格工具集，包含复制，打印，导出excel，PDF等功能
		
		'pAutoload' : true,                     //是否自动读取数据
		'pLengthMenu' : [ 10, 25, 50, 100 ],     //每页显示记录数可选下拉列表设置
		'pJavaModel' : false,                   //后台参数前缀
		'pRequestType' : 'POST',                 //请求类型，默认为POST，需要使用静态json文件做为数据源时，需要设置该参数值为GET
		'pIdColumn' : 'id',                      //指定结果集中的ID字段，默认为'id'
		'pTableClass' : 'table table-striped table-bordered dTableR table-responsive',//表格样式设置，默认为Bootstrap的样式
		
		/**
		 * 分页相关参数
		 */
		'pPageBar' : true,                      //是否显示分页栏
		'pLengthMenuBar' : true,                //每页显示记录数栏是否显示
		'pForwardPage' : -1,                     //目标转向页数
		'pPageSize' : 10,                        //设置每页显示记录数
		'pDefaultOrderColumn' : undefined,      //默认排序列，但若指定的列排序为关闭，同样设置无效
		'pSortColumn' : '',                      //排序字段
		'pSortOrder' : '',                       //排序顺序
		'pQueryType' : '',
		'pQueryString' : '',
		'params' : false,                       //后台查询参数，参数格式：[{name:'a',value:'aaa'},{name:'b',value:'bbb'},{……}]
		'url' : undefined,                      //后台数据请求URL
		
		/**
		 * 事件回调
		 */
		'eClick' : undefined,                       //行单击事件；参数（rowData行数据,rowElement行元素【TR】）
		'eDbClick' : undefined,                     //行双击事件；参数（rowData行数据,rowElement行元素【TR】）
		'eSelectRow' : undefined,                   //行选中事件；参数（rowData行数据,rowElement行元素【TR】）
		'eSelectAllRows' : undefined,               //行全选回调事件；参数（rowData行数据,数组格式,rowElement行元素【TR】,type选中状态boolean）
		'eDrawComplete' : undefined,                //表格数据处理结束后的事件回调（每一次数据请求）；参数（oSettings表格内部参数集）
		'onSuccess' : undefined,                    //DataTables初始化完成回调
		
		/**
		 * DataTables原生参数修改默认值
		 */
		'retrieve' : true,                      //若DataTables对象已存在，是否不进行初始化，直接返回已存在的DataTables对象
		'ordering': true,                       //表格排序
        //国际化支持：中文
		'language' : {
			"loadingRecords": "努力抓取数据中...",
			"processing": "数据读取中...",
			"lengthMenu": "每页 _MENU_ 条",
			"zeroRecords": "没有任何数据返回",
			"info": "当前 <strong>_PAGE_</strong> / <strong>_PAGES_</strong> 页 （共<strong>_TOTAL_</strong>条）",//共 <strong>_TOTAL_</strong> 条记录
			"infoEmpty": "0 到 0 共 0 条记录",
			"infoFiltered": "(已从 _MAX_ 条记录中过滤)",
			"infoPostFix": "",
			"search": "快速搜索",
			"paginate": {
				"sFirst":    "首页",
				"sPrevious": "«",
				"sNext":     "»",
				"sLast":     "尾页"
			}
        },
        'scrollX' : false,//默认关闭水平滚动条
        'processing' : true //是否显示正在处理中的文本
	};
	var eGrid = {
		//常量
		constants : {
			dataTableObjName : 'dtName',
			editStatus : 'editStatus',
			checkSingle : 'single',
			checkMulti : 'multi',
			checkIdPrefix : 'dt_rowcheck_',
			selectedClass : 'row_selected',
			selectedTrClass : 'tr.row_selected',
			rowIdColumn : 'DT_RowId',
			rowClassColumn : 'DT_RowClass',			
			emptyData : {'totalRow':0,'pageNumber':1,'list':[],'iTotalRecords':0,'iTotalDisplayRecords':0},
			autoHorizontalColumnLimit : 10,//自动打开水平滚动条的列个数下限
			columnAlign : [//列排序，关键字及实际使用Class定义
				{key : 'left',className : 'dt-left'},
				{key : 'center',className :'dt-center'},
				{key : 'right',className :'dt-right'}
			]
		},
		//设置表格行选中行为
		//obj：表格对象
		//check：选中类型，设置参考defaults.check的说明
		_setRowCheck : function(obj,p){
			if(p.pCheck){
				var c = eGrid.constants;
				var g = $(obj).dataTable();//.data(c.dataTableObjName);
				if(p.pCheck==c.checkSingle){
					if(p.pCheckRow){//判断是否允许点击行任意位置使得行被选中
						$("tbody tr",$(obj)).off('click.DT').on('click.DT',function(e){//设置行点击事件
							if($(this).hasClass(c.selectedClass)){
								if(p.pSingleCheckClean){//单选模式下选中行后再次单击行清除选中
									$(this).removeClass(c.selectedClass);
									if(p.pCheckColumn) $(":radio[id^='"+c.checkIdPrefix+"']",$(this)).prop('checked',false);
								}
								if(p.eClick && $.isFunction(p.eClick)) p.eClick(g.fnGetData($(this)),$(this));
							} else {
								g.$(c.selectedTrClass).removeClass(c.selectedClass);
								$(this).addClass(c.selectedClass);
								if(p.pCheckColumn){
									$(":radio[id^='"+c.checkIdPrefix+"']:checked",$(obj)).prop('checked',false);//清除其它已选中的
									$(":radio[id^='"+c.checkIdPrefix+"']",$(this)).prop('checked',true);
								}
								if(p.eClick && $.isFunction(p.eClick)) p.eClick(g.fnGetData($(this)),$(this));
								if(p.eSelectRow && $.isFunction(p.eSelectRow)) p.eSelectRow(g.api().row(this).data(),$(this));
							}
						});
					}

					if(p.pCheckColumn){//设置单选框点击事件
						$(":radio[id^='"+c.checkIdPrefix+"']",$(obj)).off('click.DT').on('click.DT',function(e){
							e.stopPropagation();
							$(":radio[id^='"+c.checkIdPrefix+"']:checked",$(obj)).prop('checked',false);
							$(c.selectedTrClass,$(obj)).removeClass(c.selectedClass);
							var tr = $(this).closest('tr');
							$(tr).addClass(c.selectedClass);
							$(this).prop('checked',true);
							
							
							//因阻止了事件冒泡，用户行点击事件需要再次调用否则点周单选框时，不会被触发
							if(p.eClick && $.isFunction(p.eClick)) p.eClick(g.fnGetData(tr),$(tr));
							if(p.eSelectRow && $.isFunction(p.eSelectRow)) p.eSelectRow(g.api().row($(tr)).data(),$(tr));
						});
					}
				}else if(p.pCheck==c.checkMulti){
					//标题栏表格，若打开了滚动条，则表格是不同对象
					var pnt;
					if(p.scrollX){
						var pbox = $(obj).closest('.dataTables_wrapper');
						pnt = $('div.dataTables_scrollHead table.dataTable',$(pbox));
					}else pnt = obj;
					
					var checkCheckAll = function(){
						if($(":checkbox[id^='"+c.checkIdPrefix+"']",$(obj)).size() == $(":checkbox[id^='"+c.checkIdPrefix+"']:checked",$(obj)).size()){
							$("#dt_row_all_check",$(pnt)).prop('checked',true);
							if(p.eSelectAllRows && $.isFunction(p.eSelectAllRows)) p.eSelectAllRows(g.api().rows().data(),$(obj),true);
						}else $("#dt_row_all_check",$(pnt)).prop('checked',false);
					};
					if(p.pCheckRow){//判断是否允许点击行任意位置使得行被选中
						$("tbody tr",$(obj)).off('click.DT').on('click.DT',function(e){//设置行点击事件
							$(this).toggleClass(c.selectedClass);
							if(p.pCheckColumn){
								var checkObj = $(":checkbox[id^='"+c.checkIdPrefix+"']",$(this));
								$(checkObj).prop('checked',!$(checkObj).prop('checked'));
								checkCheckAll();
							}
							if(p.eClick && $.isFunction(p.eClick)) p.eClick(g.fnGetData($(this)),$(this));
							if(p.eSelectRow && $.isFunction(p.eSelectRow) && $(this).hasClass(c.selectedClass)) p.eSelectRow(g.api().row(this).data(),$(this));
						});
					}
					if(p.pCheckColumn){//设置复选框点击事件
						var checkboxFunction = function(e){
							e.stopPropagation();
							var tr = $(this).closest('tr');
							if($(this).prop('checked')){
								if(!$(tr).hasClass(c.selectedClass)) $(tr).addClass(c.selectedClass);
							}else $(tr).removeClass(c.selectedClass);
							checkCheckAll();
							if(p.eClick && $.isFunction(p.eClick)) p.eClick(g.fnGetData(tr),$(tr));
							if(p.eSelectRow && $.isFunction(p.eSelectRow) && $(tr).hasClass(c.selectedClass)) p.eSelectRow(g.api().row($(tr)).data(),$(tr));
						};
						$("tbody td.selectColumn :checkbox",$(obj)).off('click.DT').on('click.DT',checkboxFunction);
						//解决IE下checkbox双击时，checkbox没有执行两次响应问题
						if(eGrid._isIE()){
							$("tbody td.selectColumn :checkbox",$(obj)).off('dblclick.DT').on('dblclick.DT',function(){
								$(this).click();
							});
						}
						
						$("thead th:eq(0)",$(pnt)).empty().append('<input type="checkbox" id="dt_row_all_check" title="全选/全取消">').attr('align','center');
						//清空标题栏
						$("#dt_row_all_check",$(pnt)).off('click.DT').on('click.DT',function(e){
							e.stopPropagation();
							if($(this).prop('checked')){
								$("tbody tr",$(obj)).addClass(c.selectedClass);
								$(":checkbox[id^='"+c.checkIdPrefix+"']",$(obj)).prop('checked',true);
								if(p.eSelectAllRows && $.isFunction(p.eSelectAllRows)) p.eSelectAllRows(g.api().rows().data(),$(obj),true);
								if(p.eSelectRow && $.isFunction(p.eSelectRow)) p.eSelectRow(g.api().rows().data(),$(obj));
							}else{
								$("tbody tr",$(obj)).removeClass(c.selectedClass);
								$(":checkbox[id^='"+c.checkIdPrefix+"']",$(obj)).prop('checked',false);
								if(p.eSelectAllRows && $.isFunction(p.eSelectAllRows)) p.eSelectAllRows(g.api().rows().data(),$(obj),false);
							}
						});
						//解决IE下checkbox双击时，checkbox没有执行两次响应问题
						if(eGrid._isIE()){
							$("#dt_row_all_check",$(pnt)).off('dblclick.DT').on('dblclick.DT',function(e){
								$(this).click();
							});
						}
					}
				}
			}
		},
		// 为表格设置事件绑定
		// obj:表格对象
		// p:参数集
		_bindEvent : function(obj,p){
			if(!obj) return;
			if(p.eDbClick && $.isFunction(p.eDbClick)){
				$("tbody tr",$(obj)).off('dblclick.DT').on('dblclick.DT',function(e){
					e.stopPropagation();
					var g = $(obj).dataTable();//.data(eGrid.constants.dataTableObjName);
					p.eDbClick(g.fnGetData(this),$(this));
				});
			}
		},
		//获得选中行，一个或多个
		//obj：表格对象(jquery)
		_getSelectedRow : function(obj){
			if(!obj) return ;
			var c = eGrid.constants;
			var dobj = $(obj).dataTable();//.data(c.dataTableObjName);
			return dobj.$(c.selectedTrClass);
		},
		//获得所有行
		_getAllRows : function(obj){
			if(!obj) return ;
			var c = eGrid.constants;
			var dobj = $(obj).dataTable();//.data(c.dataTableObjName);
			return dobj.$('tbody tr');
		},
		//设置后台查询参数
		_setQueryParams : function(p,oSettings){
        	var prefix = '';
        	if(p.pJavaModel) prefix = p.pJavaModel + '.';
			
        	var cPage = Math.ceil( oSettings._iDisplayStart / oSettings._iDisplayLength );
			//Grid查询参数
			var params = [{
				name : prefix + 'pageNumber', value : !isNaN(cPage)?cPage+1:1 //
			}, {
				name : prefix + 'pageSize', value : oSettings._iDisplayLength //iDisplayLength
			}, {
				name : prefix + 'sortname', value : p.pSortColumn
			}, {
				name : prefix + 'sortorder', value : p.pSortOrder
			}];
			/*
			, {
				name : prefix + 'query', value : p.pQueryString
			}, {
				name : prefix + 'qtype', value : p.pQueryType
			}
			 */
			if (p.params) copyProperties(params, p.params);
			return params;
		},
		//更新分页信息
		_updatePageInfo : function(params,oSettings,data){
			var prefix = oSettings.oInit.pJavaModel;
			prefix = (prefix && $.type(prefix)=='string') ? prefix + '.' : '';
			//不做处理，不能更新 _iDisplayLength ，这个在每页展示的下拉框修改的时候，会修改 _iDisplayLength的值；但不会修改oInit.pPageSize值；
			//如果重新获取 oInit的值，会导致，下拉的值被覆盖了；
			/*if(typeof(oSettings.oInit.pPageSize)!='undefined' && typeof(oSettings.oInit.pPageSize)=='number'){
				oSettings._iDisplayLength = oSettings.oInit.pPageSize;
			}*/
			var curPage = Math.ceil( oSettings._iDisplayStart / oSettings._iDisplayLength );
			//设置指定跳转页
			if(oSettings.oInit.pForwardPage!=-1){
				curPage = oSettings.oInit.pForwardPage -1;
				oSettings._iDisplayStart = (oSettings.oInit.pForwardPage -1) * oSettings._iDisplayLength;
				if(curPage < 0) curPage = 0;
				oSettings.oInit.pForwardPage = -1;
			}
			var sortname ='',sortindex=0,sortorder = '',sColumns;
			if(data && oSettings.oInit.ordering){
				/**
				 * sEcho：数字，代表数据请求的次数统计
				 * iColumns：列的总个数
				 * iDisplayStart：当前页显示起始下标
				 * iDisplayLength：每页显示记录数
				 * iSortCol_0：排序字段（列下标）
				 * sSortDir_0：排序方式[asc、desc]
				 */
				
				sortindex = data.order[0].column;
				sortorder = data.order[0].dir;
				//获得进行排序的字段名
				sortname = data.columns[sortindex].data;
				//处理默认排序方式
				if(sortname && !sortorder) sortorder = 'asc';
			}
			$.each(params,function(i,n){
				if(n.name == prefix + 'pageSize') n.value = oSettings._iDisplayLength;//更新每页显示记录数
				if(n.name == prefix + 'pageNumber') n.value = !isNaN(curPage)?curPage+1:1;//更新当前页
				if(n.name == prefix + 'sortname' && sortname)  n.value = sortname;//更新排序字段
				if(n.name == prefix + 'sortorder' && sortname) n.value = sortorder;//更新排序方式
			});
		},
		//根据URL内容返回当前连接符号应该是?还是&
		_getUrlConnCode:function(url){
			return url.indexOf('?')!=-1 ? '&' : '?';
		},
		//获得跳转式分页的跳转URL
		_getReDirectUrl : function(oSettings){
			var redirect = oSettings.sAjaxSource;
			var initParams = oSettings.initParams;
			var params = eGrid._setQueryParams(initParams, oSettings);
			$.each(params,function(i,row){
				redirect += eGrid._getUrlConnCode(redirect) + row.name + '=' + row.value;
			});
			return redirect;
		},
		//设置遮罩中文字的居中对齐
		_setProcessPadding : function(tb){
			var pDiv = $(tb).prev('.dataTables_processing');
			var pDivHeight = $(pDiv).height();
			//alert(pDivHeight);
			//字体高度40PX，实际测量高度为55
			var fontHeight = 55;
			$(pDiv).css('padding-top',(pDivHeight - fontHeight) / 2);
		},
		//转换旧版参数到新版
		_upgradeParams : function(p){
			//转换列模型
			if($.type(p.columns)=='undefined' && $.type(p.aoColumns)!='undefined' && $.isArray(p.aoColumns)){
				var columns = new Array();
				$.each(p.aoColumns, function(i, c) {
					if(c && !$.isEmptyObject(c)){
						var col = {};
						if($.type(c.mData)!='undefined') col.data = c.mData;
						if($.type(c.sTitle)!='undefined') col.title = c.sTitle;
						if($.type(c.sWidth)!='undefined') col.width = c.sWidth;
						if($.type(c.mRender)!='undefined') col.render = c.mRender;
						if($.type(c.sDefaultContent)!='undefined') col.defaultContent = c.sDefaultContent;
						if($.type(c.sClass)!='undefined') col.className = c.sClass;
						if($.type(c.bVisible)!='undefined') col.visible = c.bVisible;
						if($.type(c.bSortable)!='undefined') col.orderable = c.bSortable;
						columns.push(col);
					}
				});
				p.columns = columns;
				delete p.aoColumns;
			}
			//转换行选中回调事件
			if($.type(p.eSelectRow)=='undefined' && $.type(p.eRowSelect)!='undefined' && $.isFunction(p.eRowSelect)){
				p.eSelectRow = p.eRowSelect;
				delete p.eRowSelect;
			}
		},
		//判断当前浏览器是否为IE
		_isIE:function(){
			return navigator.userAgent.indexOf("MSIE") !== -1 || navigator.userAgent.indexOf("Trident") !== -1;
		},
		//获得Bootstrap版本，目前只判断2和3的版本区别
		_bootstrapVersion : function(){
			var v = $.fn.alert.Constructor.VERSION;
			return v ? 3 : 2;
		}
	};
	
	
	
	$.fn.extend({
		//初始化DataTables - Ajax读取数据方式
		initDT : function(params){
			var $this = $(this);
			var c = eGrid.constants;
			var p = $.extend({}, _defaults, params);
			//设置Table样式
			if(!$this.hasClass(p.pTableClass)) $this.addClass(p.pTableClass);
			//升级旧版列模型到新版
			eGrid._upgradeParams(p);
			
			//是否显示选择列
			if(p.pCheckColumn){
				if(!p.columns) p.columns = new Array();
				//若模型中已有选择列，则不再添加
				if(p.columns.length > 0 && (!p.columns[0].className || p.columns[0].className.indexOf('selectColumn') == -1)){
					p.columns.unshift({
						"data" : "",
						"defaultContent" : "",
						"title":"选择",
						//原值35PX，因表格最右边有向右超出一个px的大小，导致表格右边框消失，
						//设置样式box-sizing为border-box（默认content-box）后，选择列会收缩，文字会变成竖向排列
						"width":"40px",
						"className" : "center selectColumn",
						"orderable" : false,
			            "render": function ( data, type, row ) {//自定义拼装列的内容
			            	if(p.pCheck == c.checkMulti){//多选模式
			            		return '<input type="checkbox" id="'+c.checkIdPrefix + row.id + '" />';
			            	}else if(p.pCheck == c.checkSingle){//单选模式
			            		return '<input type="radio" id="'+c.checkIdPrefix + row.id + '" />';
			            	}
			            }
					});
				}

				//若有设置列初始化内容的，同时有选择列时，需要将列下标+1
				if(p.aoColumnDefs){
					$.each(p.aoColumnDefs,function(i,n){
						if(n.aTargets && $.isArray(n.aTargets) && n.aTargets.length>0){
							$.each(n.aTargets,function(ii,nn){
								p.aoColumnDefs[i].aTargets[ii] = nn+1;
							});
						}
					});
				}				
			}
			
			var _colAlign = false,_sortColumn = 0;
			//获得列全局对齐参数
			if(p.pColAlign){
				$.each(c.columnAlign,function(i,d){
					if(d.key == p.pColAlign){
						_colAlign = d.className;
						return false;
					}
				});
			}
			//统一设置列排列样式
			$.each(p.columns,function(i,n){
				if(n.visible == undefined || n.visible == true){
					if(_colAlign) {
						if(n.className) n.className += ' '+_colAlign;
						else n.className = _colAlign;
					}
				}
				//统计打开列排序的个数
				if(undefined !== n.orderable && n.orderable == true) _sortColumn++;
				//若没有设置是否排序，则默认设置为不排序，插件原生默认为排序
				if(undefined === n.orderable) n.orderable = false;
			});
			
			//无字段设置排序，则关闭表格排序功能
			if(_sortColumn == 0) p.ordering = false;
			else{
				/* 设置默认排序  */
				// 排除选择列和隐藏列
				var defaultSortIndex = 0;
				$.each(p.columns,function(i,n){
					if((n.visible == undefined || n.visible == true) && undefined!== n.orderable && n.orderable == true){
						if(p.pCheckColumn && i!=0){//有选择列
							defaultSortIndex = i;
							return false;
						}					
						if(!p.pCheckColumn){//无选择列
							defaultSortIndex = i;
							return false;
						}
					}
				});
				//若指定了初始化排序列，则判断该列是否打开排序，否则不影响结果
				if(undefined !== p.pDefaultOrderColumn && $.type(p.pDefaultOrderColumn)=='number'){
					var tmpIdx = p.pCheckColumn ? p.pDefaultOrderColumn + 1 : p.pDefaultOrderColumn;
					if(undefined !== p.columns[tmpIdx].orderable && p.columns[tmpIdx].orderable === true) defaultSortIndex = tmpIdx;
				}
				
				p.order = [[defaultSortIndex , 'asc']];
				//p.aaSorting = [[5 , 'asc']];
				/* 设置默认排序  */
			}
			
			var sDom = 'rt<lip<"clear">>';
			var sDomBase = '<"dataTableDataScroll"rt>';
			var sDomPage = '<pil<"clear">>';
			var sDomTool = '';
			
			// 设置表格工具集
			if(p.pTableTools){
				p.tableTools = {
					"aButtons": [
					    {"sExtends":"copy","sButtonText":"复制"},
					    {"sExtends":"print","sButtonText":"打印"},
					    {"sExtends":"xls","sButtonText":"导出Excel"},
					    {"sExtends":"pdf","sButtonText":"导出PDF"}
					],
					"sSwfPath": $webroot+"js/jquery/datatables/extras/TableTools/swf/copy_csv_xls_pdf.swf"
				};
				//sDom = 'rt<<"span3"T>lip<"clear">>';
				//sDomTool = '<"span3"T>';
			}
			/* 列拖动 */
			//似乎不可用
			//if(p.pColDrag) sDomBase = 'R' + sDomBase;
			
			/* 允许设置列是否可见的按钮 */
			if(p.pColumnVis){
				p.buttons = {
					buttons :[{
						text : '显示 / 隐藏列',
						extend: 'colvis',
		                collectionLayout: 'fixed two-column'
					}],
					dom : {}
				};
				sDomTool += 'B';
			}
			
			/* 设置每页显示记录数下拉列表 */
			if(p.pLengthMenu && $.isArray(p.pLengthMenu) && p.pLengthMenu.length > 0){
				p.lengthMenu = p.pLengthMenu;
			}
			
			//设置分页
			if(typeof(p.pPageBar)!='undefined' && typeof(p.pPageBar)=='boolean'){
				p.paging = p.pPageBar;
				sDomPage = p.pPageBar ? sDomTool + (p.pLengthMenuBar?'pil':'pi') : sDomTool;
			}
			
			//处理后台请求URL
			//使用sAjaxSoure为1.9版的使用方式，新版建议使用Ajax方式
			if(!p.url){
				p.url = p.sAjaxSource;
				delete p.sAjaxSource;
			}
			//最终布局设置
			sDom = sDomBase + '<' + sDomPage + '<"clear">>';
			p = $.extend({}, p, {
				//处理DataTables服务端数据处理完成后的事件回调
				'initComplete' : function(oSettings,json){
					//处理打开水平滚动条时，因为标题列与内容列宽度不一致问题，暂时将标题行表格关闭，并显示被隐藏的原标题行
					var mainDivId = $($this).attr('id') + '_wrapper';
					var mainDiv = $('#' + mainDivId);
					/*
					 * 在浏览器100%缩放显示模式下，显示并没有问题
					if(oSettings.oInit.scrollX){
						//$('div.dataTables_scrollHead',$(mainDiv)).hide();
						var dataBodyWidth = $('div.dataTables_scrollBody table.dataTable',$(mainDiv)).outerWidth();
						//$('div.dataTables_scrollHead div.dataTables_scrollHeadInner',$(mainDiv)).css('width','100%');
						$('div.dataTables_scrollHead div.dataTables_scrollHeadInner',$(mainDiv)).css({'width':dataBodyWidth});
					}
					*/
					
					if(params.onSuccess && $.isFunction(params.onSuccess)) params.onSuccess(oSettings,json);
		        },
		        //当每次渲染完表格后做的回调事件
		        'drawCallback': function( oSettings ) {
					//var mainDivId = $($this).attr('id') + '_wrapper';
					//var mainDiv = $('#' + mainDivId);
					/*
					if(oSettings.oInit.scrollX){
						//$('div.dataTables_scrollHead',$(mainDiv)).hide();
						var dataBodyWidth = $('div.dataTables_scrollBody table.dataTable',$(mainDiv)).width();
						//$('div.dataTables_scrollHead .dataTables_scrollHeadInner',$(mainDiv)).css('width',dataBodyWidth);
						//$('div.dataTables_scrollBody',$(mainDiv)).css('width',0);
						$('div.dataTables_scrollHead div.dataTables_scrollHeadInner',$(mainDiv)).css('width',dataBodyWidth);
						//$('div.dataTables_scrollBody',$(mainDiv)).width(dataBodyWidth);
					}
					*/
		        	if(p.pCheck) eGrid._setRowCheck($this, p);//设置行单选复选处理
		        	eGrid._bindEvent($this, p);//设置事件
		        	
		        	if(params.eDrawComplete && $.isFunction(params.eDrawComplete)) params.eDrawComplete(oSettings);
		        },
		        //后台数据请求
		        'ajax' : function (data, callback, settings){
		        	//var params = oSettings.queryParams ? oSettings.queryParams : eGrid._setQueryParams(p,oSettings);
		        	var _params = eGrid._setQueryParams(p,settings);
		        	
		        	if(settings.queryParams) _params = copyProperties(settings.queryParams,_params);
		        	settings.queryParams = _params;//缓存更新分页查询参数后的参数集合
		        	
		        	settings.initParams = p.params;//缓存初始化数据查询参数（非表格初始化参数）
		        	eGrid._updatePageInfo(_params, settings, data);//更新分页信息
		        	settings.dtInitParams = p;//将用户初始化的原始参数进行缓存，用于重新构造表格
		        	if(!p.pAutoload){
		        		callback(c.emptyData);
		        		p.pAutoload = true;
		        	}else{
			            $.eAjax({
							"dataType": 'json',
							"type": p.pRequestType,
							"url": p.url,
							"data": _params,
							"success": function(returnData){
								var result = returnData.gridResult;
								if($.isPlainObject(result)){
									//数据每个数据行增加DT_RowId字段，这是datatables默认自动识别的ID字段
									$.each(result.list,function(i,row){
										if(row && row[p.pIdColumn] && row[p.pIdColumn]!==0){
											eval('row.'+c.rowIdColumn+'="'+row[p.pIdColumn]+'"');
										}else{
											eval('row.'+c.rowIdColumn+'="'+ i +'"');
										}
									});
									
									//result.iTotalRecords = result.totalRow;
									//result.iTotalDisplayRecords = result.totalRow;
									result.draw = data.draw;
									result.recordsTotal = result.totalRow;
									result.recordsFiltered = result.totalRow;
								}
								callback(result);
							}
			            });
		        	}
		        },
		        'autoWidth': true,//设置是否自适应宽度，在部分bootstrap主题中宽度不正常的时候试着修改该属性(主要是在动态调整浏览器显示宽度时，设置为false将不会自动调整宽度)
		        'scrollCollapse': true,
		        'serverSide' : true, //是否服务端请求		        
		        'sAjaxDataProp' : "list", //服务端返回数据的json节点
		        "pagingType": "bootstrap_full",//使用bootstrap风格的样式
		        "stateSave" : false,//保存表格状态
		        "dom": sDom,// 修改界面排版，将每页显示记录数移动到底部分页条
		        //提交方式
		        'sServerMethod' : 'POST'
			});
			return this.each(function(){
				var _tb = this;
				var c = eGrid.constants;
				var dobj = $(this).dataTable(p);
				$(dobj).on( 'column-visibility.dt', function ( e, settings, column, state ) {
					e.stopPropagation();
					/*
		        	//统计当前设置为显示的列个数
					var colCount = 0;
					$.each(settings.aoColumns,function(i,row){
						if(row && row.bVisible == false){
							return true;
						}else{
							colCount++;
						}
					});
					//当动态设置列显示隐藏时，列的个数增加到一定数量，则自动打开表格水平滚动条
					if(colCount > c.autoHorizontalColumnLimit){
						var initParams = settings.dtInitParams;
						initParams.scrollX = true;
						if(initParams.pCheckColumn){
							initParams.aoColumns = $.grep(settings.aoColumns,function(row,i){
								//排除选择列
								//console.log(row && row.sClass && row.sClass.indexOf('selectColumn')==-1);
								return (row && (!row.sClass || (row.sClass && row.sClass.indexOf('selectColumn')==-1)));
							});
							//alert(initParams.aoColumns.length);
						}else{
							initParams.aoColumns = settings.aoColumns;
						}
						initParams.aoColumns = settings.aoColumns;
						initParams.destroy = true;
						//console.log(initParams.aoColumns);
						//dobj.api().destroy();
						//dobj = null;
						dobj = $(_tb).initDT(initParams);
					}
					*/
				}).on( 'column-sizing.dt', function ( e, settings ) {
					//增加了该事件，什么都不做，页面尺寸变化后，表格的标题栏不会出问题
					//设置该事件，则会出现尺寸不对称的问题
					$.noop();
				}).on('processing.dt',function(e, settings, processing){
					//设置提示遮罩内边距高度
		        	if(p.processing && processing) eGrid._setProcessPadding(_tb);
				});
				//缓存表格对象
				//$(this).data(c.dataTableObjName,dobj);
				$(this).removeClass('no-footer');
				
				//设置列进行宽度手动调整
				if(p.pColResize && $.fn.colResizable) $(this).colResizable({});
			});
		},
		//根据表单内容查询表格数据(后台)
		gridSearch : function(p){
			return this.each(function() {
				if($.fn.DataTable.fnIsDataTable(this)){
					var g = $(this).dataTable();//$(this).data(eGrid.constants.dataTableObjName);
					var setting = g.fnSettings();
					if(setting && setting.queryParams){
						copyProperties(setting.queryParams, p);
						g.fnDraw();
					}
				}
			});
		},
		//刷新表格
		gridReload : function(){
			return this.each(function(){
				if($.fn.DataTable.fnIsDataTable(this)){
					var g = $(this).dataTable();//.data(eGrid.constants.dataTableObjName);
					var setting = g.fnSettings();
					g.fnDraw(setting);
				}
			});
		},
		//获得表格的内部对象
		getDTObj : function(){
			return $(this).dataTable();//.data(eGrid.constants.dataTableObjName);
		},
		/**
		 * 自动调整列宽度
		 * 
		 * 
		 * 在多个Tab下有多个表格时，可以使用以下的方法在Tab切换时执行
		 * 目的为在切换Tab时执行表重新调整
		 * $.fn.dataTable.tables( {visible: true, api: true} ).columns.adjust();
		 */
		columnAdjust : function(){
			var g = $(this).dataTable();//.data(eGrid.constants.dataTableObjName);
			//.draw()不能带上，若带上会再进行一次后台请求
			g.api().columns.adjust();
		},
		//获得选中行，一个或多个
		getSelectedDOM : function(){
			return eGrid._getSelectedRow($(this));
		},
		//获得选中行数据，一个或多个
		//格式：
		//[{id:1,cell:[xx,xx,...]},
		//{id:2,cell:[xx,xx,...]},
		//{id:3,cell:[xx,xx,...]},
		//{id:4,cell:[xx,xx,...]}]
		getSelectedData : function(){
			var g = $(this).dataTable();//.data(eGrid.constants.dataTableObjName);
			var rows = eGrid._getSelectedRow($(this));
			if(rows && rows.size()>0){
				var arr = new Array();
				$.each(rows,function(i,row){
					arr.push(g.fnGetData(row));
				});
				return arr;
			}else{
				return false;
			}
		},
		//获得所有行数据
		//格式：
		//[{id:1,cell:[xx,xx,...]},
		//{id:2,cell:[xx,xx,...]},
		//{id:3,cell:[xx,xx,...]},
		//{id:4,cell:[xx,xx,...]}]
		getAllData : function(){
			var g = $(this).dataTable();//.data(eGrid.constants.dataTableObjName);
			var rows = eGrid._getAllRows($(this));
			if(rows && rows.size()>0){
				var arr = new Array();
				$.each(rows,function(i,row){
					arr.push(g.fnGetData(row));
				});
				return arr;
			}else{
				return false;
			}
		},
		//行编辑
		rowEdit : function(){
			if($(this).data(c.editStatus)){
				var msg = '表格当前已处于编辑状态！';
				if(window.eDialog){
					eDialog.alert(msg,function(){
						return ;
					});
				}else{
					alert(msg);
					return;
				}
			}
			var api = $(this).DataTable();
			var c = eGrid.constants;
			var g = $(this).dataTable();//.data(c.dataTableObjName);
			var s = g.fnSettings();
			var row = eGrid._getSelectedRow($(this));
			var d = g.fnGetData(row);
			var column = s.oInit.columns ? s.oInit.columns : s.oInit.aoColumns;
			//console.log(d);
			//debugger
			if(row && row.length == 1){
				//console.log(column.length);
				//console.log($('td',$(row)).size());
				var count = 0;
				$.each(column,function(i,n){
					if(n.editInit && $.isFunction(n.editInit)){
						var cell = $("td:eq("+i+")",row);
						var cdata = api.cell(cell).data();
						/**
						 * columns列模型中，为列增加editInit的参数
						 * 用于进行单元格可编辑内容处理
						 * 
						 * 类型：function
						 * 参数：
						 * data：当前单元格数据内容
						 * type：当前单元格数据内容的数据类型
						 * row：当前行的所有数据
						 * rowDom：当前行的DOM元素
						 */
						$(cell).html(n.editInit(cdata,$.type(cdata),d,row));
						count++;
					}
				});
				if(count > 0) $(this).data(c.editStatus,true);
			}else if(!row || row.length == 0){
				var msg = '请至少选择一行进行操作！';
				window.eDialog ? eDialog.alert(msg) : alert(msg);
			}else if(row && row.length > 1){
				var msg = '只能选择一行进行操作！';
				window.eDialog ? eDialog.alert(msg) : alert(msg);
			}
		},
		//获得行编辑表单
		//参数：
		//callback：编辑表单数据保存回调方法
		//          返回值，Object
		//          {
		//          	id : xxx,行ID
		//              data : {},原行数据
		//              dom  : jquery object，当前行DOM元素
		//          }
		getRowForm : function(){
			if(!$(this).data(c.editStatus)) return null;
			var api = $(this).DataTable();
			var c = eGrid.constants;
			var g = $(this).dataTable();//.data(c.dataTableObjName);
			var row = eGrid._getSelectedRow($(this));
			var d = api.row(row).data();
			var result;
			if(row && row.length == 1){
				result = {
					id : d[c.rowIdColumn],
					data : d,
					dom : row
				};
			}else if(!row || row.length == 0){
				var msg = '请至少选择一行进行操作！';
				window.eDialog ? eDialog.alert(msg) : alert(msg);
			}else if(row && row.length > 1){
				var msg = '只能选择一行进行操作！';
				window.eDialog ? eDialog.alert(msg) : alert(msg);
			}
			return result;
		},
		//表单编辑完成(执行表格刷新)
		rowEditDone : function(){
			if($(this).data(c.editStatus)){
				$(this).data(c.editStatus,false);//关闭编辑状态
				$(this).gridReload();
			}
		},
		//获得当前页号
		getCurrentPage : function(){
			var g = $(this).dataTable();//.data(eGrid.constants.dataTableObjName);
			var oSettings = g.fnSettings();
			return Math.ceil( oSettings._iDisplayStart / oSettings._iDisplayLength ) +1;
		},
		//设置下次一查询的指定跳转页数
		setForwardPage : function(page){
			if(typeof(page) == 'number' && page > 0){
				var g = $(this).dataTable();//.data(eGrid.constants.dataTableObjName);
				var oSettings = g.fnSettings();
				oSettings.oInit.pForwardPage = page;
			}
		},
		//获得当前设置的每页显示记录数
		getPageSize : function(){
			var g = $(this).dataTable();//.data(eGrid.constants.dataTableObjName);
			var oSettings = g.fnSettings();
			return oSettings._iDisplayLength;
		},
		//获得选中行的行下标
		getCheckedRowsIndex : function() {
			var arr = new Array();
			$("tbody tr." + eGrid.constants.selectedClass,$(this)).each(function(i,row){
				arr.push($(this).index());
			});
			return arr;
		},
		//获得选中行的ID数组
		getCheckIds : function(){
			var arr = new Array();
			$("tbody tr." + eGrid.constants.selectedClass,$(this)).each(function(i,row){
				arr.push($(this).attr('id'));
			});
			return arr;
		},
		//手动选中所有行(当前页)
		selectAllRow : function() {
			var $this = $(this);
			var c = eGrid.constants;
			$("tbody tr",$this).addClass(c.selectedClass);
			$(":checkbox[id^='"+c.checkIdPrefix+"']",$this).prop('checked',true);
			$("#dt_row_all_check",$this).prop('checked',true);			
		},
		//取消选中所有行
		unSelectAllRow : function() {
			var $this = $(this);
			var c = eGrid.constants;
			$("tbody tr",$this).removeClass(c.selectedClass);
			$(":checkbox[id^='"+c.checkIdPrefix+"']",$this).prop('checked',false);
			$("#dt_row_all_check",$this).prop('checked',false);
		},
		//下一页
		pageNext : function(){
			var g = $(this).dataTable();//.data(eGrid.constants.dataTableObjName);
			g.fnPageChange('next');
		},
		//上一页
		pagePrevious : function(){
			var g = $(this).dataTable();//.data(eGrid.constants.dataTableObjName);
			g.fnPageChange('previous');
		},
		//第一页
		pageFirst : function(){
			var g = $(this).dataTable();//.data(eGrid.constants.dataTableObjName);
			g.fnPageChange('first');
		},
		//最后一页
		pageLast : function(){
			var g = $(this).dataTable();//.data(eGrid.constants.dataTableObjName);
			g.fnPageChange('last');
		},
		// 在页面上添加一行，仅限页面操作   *不可用
		addRow : function(data,id){
			var g = $(this).dataTable();//.data(eGrid.constants.dataTableObjName);
			var row = null;
			if(id!=undefined && id!=null && id!=''){
				row = {
					"id" : id,
					"cell" : data,
					"DT_RowId" : id
				};
			}else{ 
				var tid = 'dt_temp_row_id_' + Math.ceil(Math.random() * 100);
				row = {
					"id" : tid,
					"cell" : data,
					"DT_RowId" : tid
				};
			}
			g.fnAddData(row);
		},
		// 在页面上删一行记录，仅限页面操作     *不可用
		removeRow : function(idx){
			var g = $(this).dataTable();//.data(eGrid.constants.dataTableObjName);
			if(idx!=undefined && idx!=='' && idx!=null && typeof(idx)=='number' && g){				
				g.fnSettings().oInit.bStaticControl = true;
				g.fnDeleteRow(idx);
				g.fnFilter('a');
				//$("tbody tr:eq("+idx+")",$(this)).remove();
			}
		}
	});
	
	$.extend( $.fn.dataTableExt.oApi, {
		//使用参数集重绘表格
		fnDrawByNewSettings : function(oSettings){
			this.oApi._fnCalculateEnd( oSettings );
			this.oApi._fnDraw( oSettings );
		}
	});
	
	//设置部分按钮的默认样式
	$.extend(true ,$.fn.dataTable.Buttons.defaults,{
		dom : {
			button : {
				className : 'btn'
			}
		}
	});
	
	
	/* Bootstrap style pagination control extend by Terry */
	$.extend( $.fn.dataTableExt.oPagination, {
		//在官方提供的分页条基础上，增加首页，尾页的按钮
		"bootstrap_full": {
			"fnInit": function( oSettings, nPaging, fnDraw ) {
				var oLang = oSettings.oLanguage.oPaginate;
				var fnClickHandler = function ( e ) {
					e.preventDefault();
					if ( oSettings.oApi._fnPageChange(oSettings, e.data.action) )  fnDraw( oSettings );
				};

				var htmlstr = '<ul>';
				//根据初始化参数处理是否隐藏每页显示记录列表和分页信息框是否显示
				/*
				if(oSettings.oFeatures.bLengthChange)
					htmlstr += '<li class="prev disabled" id="dt_pageinfo_list"><a>&nbsp;</a></li>';
				if(oSettings.oFeatures.bInfo)
					htmlstr += '<li class="prev disabled" id="dt_pageinfo_count"><a href="javascript:void(0);">&nbsp</a></li>';
				*/
				htmlstr += '<li class="prev disabled" id="dt_pageinfo_first" title="首页"><a href="javascript:void(0);">'+oLang.sFirst+'</a></li>'+
						   '<li class="prev disabled" id="dt_pageinfo_previous"><a href="javascript:void(0);">'+oLang.sPrevious+'</a></li>'+//&larr;
						   '<li class="next disabled" id="dt_pageinfo_next"><a href="javascript:void(0);">'+oLang.sNext+'</a></li>'+//&rarr;
						   '<li class="next disabled" id="dt_pageinfo_last" title="尾页"><a href="javascript:void(0);">'+oLang.sLast+'</a></li>'+//'+oLang.sLast+'
						   '</ul>';
				$(nPaging).addClass('pagination').append(htmlstr);
				$('#dt_pageinfo_first',nPaging).off('click.DT').on( 'click.DT', { action: "first" }, fnClickHandler );
				$('#dt_pageinfo_previous',nPaging).off('click.DT').on( 'click.DT', { action: "previous" }, fnClickHandler );
				$('#dt_pageinfo_next',nPaging).off('click.DT').on( 'click.DT', { action: "next" }, fnClickHandler );
				$('#dt_pageinfo_last',nPaging).off('click.DT').on( 'click.DT', { action: "last" }, fnClickHandler );
			},

			"fnUpdate": function ( oSettings, fnDraw ) {
				var iListLength = 5;
				var oPaging = oSettings.oInstance.fnPagingInfo();
				var an = oSettings.aanFeatures.p;
				var i, j, sClass, iLen, iStart, iEnd, iHalf=Math.floor(iListLength/2);

				if ( oPaging.iTotalPages < iListLength) {
					iStart = 1;
					iEnd = oPaging.iTotalPages;
				} else if ( oPaging.iPage <= iHalf ) {
					iStart = 1;
					iEnd = iListLength;
				} else if ( oPaging.iPage >= (oPaging.iTotalPages-iHalf) ) {
					iStart = oPaging.iTotalPages - iListLength + 1;
					iEnd = oPaging.iTotalPages;
				} else {
					iStart = oPaging.iPage - iHalf + 1;
					iEnd = iStart + iListLength - 1;
				}

				for ( i=0, iLen=an.length ; i<iLen ; i++ ) {
					// Remove the middle elements
					//$('li:gt(0)', an[i]).filter(':not(:last)').remove();
					//$('li:gt(3)', an[i]).filter(':not(#dt_pageinfo_next,#dt_pageinfo_last)').remove();
					$('li:not(#dt_pageinfo_list,#dt_pageinfo_count,#dt_pageinfo_first,#dt_pageinfo_previous,#dt_pageinfo_next,#dt_pageinfo_last)', an[i]).remove();
					// Add the new list items and their event handlers
					for ( j=iStart ; j<=iEnd ; j++ ) {
						sClass = (j==oPaging.iPage+1) ? 'class="active"' : '';
						var curPage = $('<li '+sClass+'><a href="javascript:void(0);">'+j+'</a></li>').insertBefore( $('#dt_pageinfo_next', an[i])[0] );
						if(j!=oPaging.iPage+1){
							$(curPage).off('click.DT').on('click.DT', function (e) {
								e.preventDefault();
								oSettings._iDisplayStart = (parseInt($('a', $(this)).text(),10)-1) * oPaging.iLength;
								fnDraw( oSettings );
							});
						}
					}

					// Add / remove disabled classes from the static elements
					if ( oPaging.iPage === 0 ) {
						$('#dt_pageinfo_first,#dt_pageinfo_previous', an[i]).addClass('disabled');
					} else {
						$('#dt_pageinfo_first,#dt_pageinfo_previous', an[i]).removeClass('disabled');
					}

					if ( oPaging.iPage === oPaging.iTotalPages-1 || oPaging.iTotalPages === 0 ) {
						$('#dt_pageinfo_last,#dt_pageinfo_next', an[i]).addClass('disabled');
					} else {
						$('#dt_pageinfo_last,#dt_pageinfo_next', an[i]).removeClass('disabled');
					}
				}
			}
		}
	});
})(window.jQuery);