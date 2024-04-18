require(["esri/map", "dojo/dom", "dojo/on", "esri/layers/ArcGISDynamicMapServiceLayer", "dojo/query", "esri/toolbars/navigation", "esri/dijit/OverviewMap", "esri/tasks/RouteTask", "esri/tasks/RouteParameters", "esri/tasks/FeatureSet", "esri/SpatialReference", "esri/layers/GraphicsLayer", "esri/symbols/SimpleLineSymbol", "esri/symbols/SimpleMarkerSymbol", "esri/symbols/SimpleFillSymbol", "esri/Color", "esri/geometry/Point", "esri/graphic", "dojo/domReady!"], funDynamicLayer);
export function funDynamicLayer(Map, dom, on, ArcGISDynamicMapServiceLayer, query, Navigation, OverviewMap, RouteTask, RouteParameters, FeatureSet, SpatialReference, GraphicsLayer, SimpleLineSymbol, SimpleMarkerSymbol, SimpleFillSymbol, Color, Point, Graphic) {
    //加载地图
    var map = new Map("mapDiv", { logo: false });
    var dLayerURL = "http://localhost:6080/arcgis/rest/services/njtech/MapServer";
    var dLayer = new ArcGISDynamicMapServiceLayer(dLayerURL);
    map.addLayer(dLayer);
    //显示隐藏图层，dojo
    on(dom.byId("btnHide"), "click", funHide);
    on(dom.byId("btnShow"), "click", funShow);
    function funHide() {
        dLayer.setVisibleLayers([1]);
    }
    function funShow() {
        dLayer.setVisibleLayers([0, 1, 2, 3]);
    }
    //地图基本操作
    //创建地图操作对象
    var navToolBar = new Navigation(map);
    //给按钮绑定单击事件
    query("button").on("click", funNavigation);
    function funNavigation(event) {
        //获取按钮文本
        var value = this.innerHTML;
        switch (value) {
            case "拉框放大":
                navToolBar.activate(Navigation.ZOOM_IN);
                break;
            case "拉框缩小":
                navToolBar.activate(Navigation.ZOOM_OUT);
                break;
            case "平移":
                navToolBar.activate(Navigation.PAN);
                break;
            case "全图":
                navToolBar.zoomToFullExtent();
                break;
            case "上一视图":
                navToolBar.zoomToPrevExtent();
                break;
            case "下一视图":
                navToolBar.zoomToNextExtent();
                break;
            case "漫游":
                navToolBar.deactivate();
                break;
        }
    }
    //鹰眼
    //attachTo属性指定鹰眼图绑定的位置，可以填写top-right,bottom-right,bottom-left和top-left；map属性指定鹰眼图中显示的地图;visible属性指定鹰眼图初始显示或隐藏的状态
    var overviewMapDijit = new OverviewMap({ attachTo: "bottom-right", map: map, visible: true });
    //启用鹰眼图
    overviewMapDijit.startup();

    var graphicsLayer = new GraphicsLayer();
    map.addLayer(graphicsLayer);

    var lSymbol = new SimpleLineSymbol(SimpleLineSymbol.STYLE_SOLID, new Color([0, 255, 0]), 2);
    var pSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.STYLE_CIRCLE, 8, lSymbol, new Color([255, 0, 0]));
    var fSymbol = SimpleFillSymbol(SimpleFillSymbol.STYLE_SOLID, lSymbol, new Color([0, 255, 255]));

    var routeParams = new RouteParameters();
    routeParams.stops = new FeatureSet();
    routeParams.returnRoutes = true;
    routeParams.returnDirections = false;
    routeParams.directionsLengthUnits = 'esriMeters';
    routeParams.outSpatialReference = new SpatialReference({ wkid: 4326 });


    on(dom.byId("btnShortestPath"), "click", function () {
        var url = "http://localhost:6080/arcgis/rest/services/njtech/NAServer/Route"
        var routeTask = new RouteTask(url);
        routeTask.solve(routeParams, showRoute, showErr);
    })
    function showRoute(routeResult) {
        alert('路径分析完成！');
        var graphic = routeResult.routeResults[0].route;
        graphic.setSymbol(lSymbol);
        graphicsLayer.add(graphic);
    }
    function showErr(err) {
    }
    on(map, "click", function (event) {
        var geometry = new Point({ "x": event.mapPoint.x, "y": event.mapPoint.y, "spatialReference": new SpatialReference({ wkid: 4326 }) });
        var graphic = new Graphic(geometry, pSymbol);
        graphicsLayer.add(graphic);
        routeParams.stops.features.push(graphic);
        //routeParams.stops.features=[{"geometry":{"x":event.mapPoint.x,"y":event.mapPoint.y,"spatialReference": new SpatialReference({ wkid: 102113 })}, "attributes":{"Id":"018-00","SV":"","Num":"4 A"}}];
    })
}