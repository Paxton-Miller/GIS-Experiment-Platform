const getExtent = {
    async findFeature(evt, map, njtech_info_Layer) {
        var view = map.getView();
        var viewResolution = view.getResolution();
        var source = njtech_info_Layer.getSource();
        var url = source.getFeatureInfoUrl(
            evt.coordinate, viewResolution, view.getProjection(),
            { 'INFO_FORMAT': 'text/html', 'FEATURE_COUNT': 50 });
        if (url) {
            let data = await fetch(url).then(function (res) {
                return res.text(); //返回Promise
            });
            return this.TableToJson(data)
        }
    },

    TableToJson(data) { //tableid是你要转化的表的表名，是一个字符串，如"example"
        data.replace('<br/>', '')
        var table, name_start_index, name_end_index
        var jsons = []
        var isNone = data.indexOf('<table')
        if (isNone == -1) { return 'none' }
        var start_index = data.indexOf('<table class="featureInfo">')
        var end_index = data.lastIndexOf('</table>')
        var tables = data.substring(start_index, end_index + 8)
        var count = tables.split('</table>').length - 1
        for (var i = 0; i < count; i++) {
            var obj = {}, ths = [], tds = [], th_count, td_count, th_start_index, th_end_index, td_start_index, td_end_index
            start_index = tables.indexOf('<table class="featureInfo">')
            end_index = tables.indexOf('</table>')
            table = tables.substring(start_index, end_index + 8)
            tables = tables.substring(end_index + 8)
            name_start_index = table.indexOf('<caption class="featureInfo">')
            name_end_index = table.indexOf('</caption>')
            obj.table_name = table.substring(name_start_index + 29, name_end_index)
            th_start_index = table.indexOf('<th>')
            th_end_index = table.lastIndexOf('</th>')
            var ths_text = table.substring(th_start_index + 4, th_end_index + 5)
            th_count = ths_text.split('/').length - 1
            for (var m = 0; m < th_count; m++) {
                th_end_index = ths_text.indexOf('</th>')
                var th = ths_text.substring(0, th_end_index)
                ths.push(th)
                ths_text = ths_text.substring(th_end_index + 5)
                th_start_index = ths_text.indexOf('<th >')
                // th_end_index = ths_text.lastIndexOf('</th>')
                ths_text = ths_text.substring(th_start_index + 5)
            }
            td_start_index = table.indexOf('<td>')
            td_end_index = table.lastIndexOf('</td>')
            var tds_text = table.substring(td_start_index + 4, td_end_index + 5)
            td_count = tds_text.split('/').length - 1
            for (var n = 0; n < td_count; n++) {
                td_end_index = tds_text.indexOf('</td>')
                var td = tds_text.substring(0, td_end_index)
                tds.push(td)
                tds_text = tds_text.substring(td_end_index + 5)
                td_start_index = tds_text.indexOf('<td>')
                tds_text = tds_text.substring(td_start_index + 4)
            }
            for (var j = 0; j < ths.length; j++) {
                obj[ths[j]] = tds[j]
            }
            jsons.push(obj)
        }
        return jsons;
    }
}
export default getExtent;
