/**
 * 基于 jquery 写的，用于行政区域/业务区域的三级联动效果；
 */
(function($) {

	/**
	 * 解析页面上元素的参数；
	 */
	var parseOptions = function(el, opts) {
		var htmlOpts = {
			children : $(el).attr("area-children"),
			type : $(el).attr("area-type"),
			level : $(el).attr("area-level"),
			defaultValue : $(el).attr("area-defaultvalue")
		};
		if (opts) {
			return $.extend({}, $.fn.area.defaultOpts, htmlOpts, opts);
		} else {
			return $.extend({}, $.fn.area.defaultOpts, htmlOpts);
		}
	};

	$.areaPlugin = {

		/**
		 * 省分编码信息，固定的，基本上不会有调整，所以直接列出来；不需要在ajax处理；
		 */
		provinceAreaInfos : {
			"admin" : {
				"110000" : "北京",
				"120000" : "天津",
				"130000" : "河北",
				"140000" : "山西",
				"150000" : "内蒙古",
				"210000" : "辽宁",
				"220000" : "吉林",
				"230000" : "黑龙江",
				"310000" : "上海",
				"320000" : "江苏",
				"330000" : "浙江",
				"340000" : "安徽",
				"350000" : "福建",
				"360000" : "江西",
				"370000" : "山东",
				"410000" : "河南",
				"420000" : "湖北",
				"430000" : "湖南",
				"440000" : "广东",
				"450000" : "广西",
				"460000" : "海南",
				"500000" : "重庆",
				"510000" : "四川",
				"520000" : "贵州",
				"530000" : "云南",
				"540000" : "西藏",
				"610000" : "陕西",
				"620000" : "甘肃",
				"630000" : "青海",
				"640000" : "宁夏",
				"650000" : "新疆",
				"710000" : "台湾",
				"810000" : "香港",
				"820000" : "澳门"
			},

		},
		/**
		 * 各自返回的数据为： [areaCode:,areaName:,childrenList: ---省级 [
		 * {areaCode:,areaName:,childrenList: --地市级 [ {areaCode:,areaName:}
		 * --区县级 ] } ] ]
		 */
		areaInfos : {
			"admin" : {},
			"busi" : {}
		},
		
		/**
		 * 从服务端加载数据；
		 * @param opts
		 */
		loadChilds : function(opts) {
			var options = $.extend({
				type : "busi",	areaCode : "", defaultvalue : "",
				callback : false,	select : false}, opts);

			var url = GLOBAL.WEBROOT + "/area/fetchChilds";

			$.eAjax({
				url : url,
				data : [ {
					name : "type",
					value : options.type
				}, {
					name : "areaCode",
					value : options.areaCode
				} ],
				type : "post",
				dataType : "json",
				success : function(data) {
					var key = options.areaCode;
					$.areaPlugin.areaInfos[options.type][key] = data;
					if ($.isFunction(options.callback)) {
						options.callback(data, options.defaultvalue,
								options.select);
					}
				}
			});
		},

		/**
		 * 根据省分编码获取地市列表信息；从目前的data中获取，如果获取不到，就取ajax，回调处理；
		 * 
		 * 入参：type --- 数据类型；privince ---省分编码；callback --数据回调处理；select --下拉列表:
		 */
		queryCitys : function(opts) {
			var options = $.extend({
				type : "busi",
				areaCode : "",
				"defaultvalue" : "",
				callback : false,
				select : false
			}, opts);
			var key = options.areaCode;
			var data = $.areaPlugin.areaInfos[options.type][key];
			if (data && $.isArray(data)) {
				if ($.isFunction(options.callback)) {
					options
							.callback(data, options.defaultvalue,
									options.select);
				}
			} else {
				if ($.isFunction(options.callback)) {
					$.areaPlugin.loadChilds(options);
				}
			}
		},

		/**
		 * 加载地市列表信息
		 */
		loadCityList : function(opts) {
			var options = $.extend({
				type : "admin",
				areaCode : "00",
				citySelect : false
			}, opts);
			if (!options.citySelect) {
				return;
			}
			
			//设置当前地市select的归属省分编码为：areaCode;
			$select = $("#" + options.citySelect);
			$select.attr("area-province", options.areaCode);
			// /获取地市列表的展示控制，是否展示空值 和 默认值 ；
			var options = $.extend(options, {
				showblank : $select.attr("area-showblank"),
				defaultvalue : $select.attr("area-defaultvalue")
			});

			$select.empty();
			if (options.showblank && options.showblank == "true") {
				$select.append("<option value=''>请选择</>");
			}
			$.areaPlugin.queryCitys({
				"type" : options.type,
				"areaCode" : options.areaCode,
				"select" : $select,
				"defaultvalue" : options.defaultvalue,
				"callback" : $.areaPlugin.displayArea
			});
		},

		/**
		 * 用于在下拉框中展示数据；
		 * 
		 * @param data
		 * @param $select
		 */
		displayArea : function(data, defaultvalue, $select) {
			if (data && $.isArray(data)) {
				$.each(data, function(i, n) {
					if (n.areaCode == defaultvalue) {
						$select.append("<option value='" + n.areaCode
								+ "' selected = 'selected'>" + n.areaName
								+ "</>");
					} else {
						$select.append("<option value='" + n.areaCode + "'>"
								+ n.areaName + "</>");
					}
				});
			};
			
			$select.change();
		},

		/**
		 * 加载区县列表信息
		 */
		loadCountyList : function(opts) {
			var options = $.extend({
				type : "admin",
				province : "00",
				city : "00",
				countySelect : false
			}, opts);

			if (!options.countySelect) {
				return;
			}

			$select = $("#" + options.countySelect);
			$select.attr({
				"area-city" : options.city,
				"area-province" : options.province
			});
			// /获取区县列表的展示控制，是否展示空值 和 默认值 ；
			var options = $.extend(options, {
				showblank : $select.attr("area-showblank"),
				defaultvalue : $select.attr("area-defaultvalue")
			});

			$select.empty();
			if (options.showblank && options.showblank == "true") {
				$select.append("<option value=''>请选择</>");
			}
			$.areaPlugin.queryCitys({
				"type" : options.type,
				"areaCode" : options.city,
				"defaultvalue" : options.defaultvalue,
				"select" : $select,
				"callback" : $.areaPlugin.displayArea
			});
		},

		/**
		 * 加载省分数据
		 * 
		 * @param opts
		 */
		loadProvinceList : function(opts) {
			var options = $.extend({
				type : "admin",
				provinceSelect : false
			}, opts);

			if (!options.provinceSelect) {
				return;
			}

			$select = $("#" + options.provinceSelect);
			// /获取地市列表的展示控制，是否展示空值 和 默认值 ；
			var options = $.extend(options, {
				showblank : $select.attr("area-showblank"),
				defaultvalue : $select.attr("area-defaultvalue"),
				showall : $select.attr("area-showall")
			});

			$select.empty();
			if (options.showblank && options.showblank == "true") {
				$select.append("<option value=''>请选择</>");
			}
			if (options.showall && options.showall == "true") {
				$select.append("<option value='156'>全国</>");
			}

			// 省分数据处理；
			$.each($.areaPlugin.provinceAreaInfos[options.type],
					function(i, n) {
						if (i == options.defaultvalue) {
							$select.append("<option value='" + i
									+ "' selected = 'selected'>" + n + "</>");
						} else {
							$select.append("<option value='" + i + "'>" + n
									+ "</>");
						}
					});
			// /触发一次变更；
			$select.change();

		}
	};

	$.fn.area = function(opts) {
		return this.each(function() {
			var options = parseOptions(this, opts);

			/**
			 * 考虑数据的初始化操作
			 */
			if (options.level == "province") {

				// 先绑定 onChange 事件；
				$(this).change(function() {
					$.areaPlugin.loadCityList({
						type : options.type,
						areaCode : $(this).val(),
						citySelect : options.children
					});
				});

				// /如果是省级；初始化下拉框数据；并调用一次select事件；
				$.areaPlugin.loadProvinceList({
					type : options.type,
					provinceSelect : $(this).attr("id")
				});

			} else if (options.level == "city") {
				// 先绑定change事件；
				$(this).change(function() {
					$.areaPlugin.loadCountyList({
						type : options.type,
						province : $(this).attr("area-province"),
						city : $(this).val(),
						countySelect : options.children
					});
				});

				// /如果是地市级，而且，该select 不作为其它 select 的子节点，那么需要考虑该数据初始化；
				if ($(
						".jquery-area[area-children='" + $(this).attr("id")
								+ "']").size() == 0) {
					$.areaPlugin.loadCityList({
						type : options.type,
						areaCode : $(this).attr("area-province"),
						citySelect : $(this).attr("id")
					});
					
					$(this).change();
				}

				

			} else if (options.level == "county") {
				// /区县级，仅考虑数据初始化，而不处理数据联动的哦；
				if ($(
						".jquery-area[area-children='" + $(this).attr("id")
								+ "']").size() == 0) {
					// /如果当前是区县级；需要初始化；
					$.areaPlugin.loadCountyList({
						type : options.type,
						province : $(this).attr("area-province"),
						city : $(this).attr("area-city"),
						countySelect : $(this).attr("id")
					});
				}
			}

			return $(this);
		});
	};

	/**
	 * 默认参数
	 */
	$.fn.area.defaultOpts = {
		children : "",
		level : "province",
		type : "admin"
	};

})(jQuery)

/**
 * 页面初始化处理
 */
$(function() {
	// 按照省、地市、区县三级的顺序进行初始化和 change 事件调用；
	$(".jquery-area[area-level='county']").area();
	$(".jquery-area[area-level='city']").area();
	$(".jquery-area[area-level='province']").area();
	
	
})