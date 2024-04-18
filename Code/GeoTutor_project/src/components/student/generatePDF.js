const generatePdf = {
    exportPdf(map) {
        map.once('rendercomplete', () => {
            // 获取需要导出的地图容器元素
            const mapContainer = document.getElementById('map');
            const mapCanvas = document.createElement('canvas');
            const size = map.getSize();
            mapCanvas.width = size[0];
            mapCanvas.height = size[1];
            // 计算图片在PDF里应该显示的尺寸
            const pdfWidth = size[0]; // A4纸的宽度，单位为毫米
            const pdfHeight = size[1]; // A4纸的高度，单位为毫米
            const mapWidth = mapContainer.offsetWidth;
            const mapHeight = mapContainer.offsetHeight;
            const mapRatio = mapWidth / mapHeight;
            let pdfImageWidth, pdfImageHeight;
            if (mapRatio > pdfWidth / pdfHeight) {
                pdfImageWidth = pdfWidth;
                pdfImageHeight = mapHeight / mapWidth * pdfWidth;
            } else {
                pdfImageHeight = pdfHeight;
                pdfImageWidth = mapWidth / mapHeight * pdfHeight;
            }
            // 计算图片应该在PDF页面中的位置
            const pdfImageX = (pdfWidth - pdfImageWidth) / 2;
            const pdfImageY = (pdfHeight - pdfImageHeight) / 2;
            const mapContext = mapCanvas.getContext('2d');
            Array.prototype.forEach.call(
                map.getViewport().querySelectorAll('.ol-layer canvas, canvas.ol-layer'),
                function (canvas) {
                    if (canvas.width > 0) {
                        const opacity =
                            canvas.parentNode.style.opacity || canvas.style.opacity;
                        mapContext.globalAlpha = opacity === '' ? 1 : Number(opacity);
                        const backgroundColor = canvas.parentNode.style.backgroundColor;
                        if (backgroundColor) {
                            mapContext.fillStyle = backgroundColor;
                            mapContext.fillRect(0, 0, canvas.width, canvas.height);
                        }
                        let matrix;
                        const transform = canvas.style.transform;
                        if (transform) {
                            matrix = transform
                                .match(/^matrix\(([^\(]*)\)$/)[1]
                                .split(',')
                                .map(Number);
                        } else {
                            matrix = [
                                parseFloat(canvas.style.width) / canvas.width,
                                0,
                                0,
                                parseFloat(canvas.style.height) / canvas.height,
                                0,
                                0,
                            ];
                        }
                        CanvasRenderingContext2D.prototype.setTransform.apply(
                            mapContext,
                            matrix
                        );
                        mapContext.drawImage(canvas, 0, 0);
                    }
                }
            );
            mapContext.globalAlpha = 1;
            // 导出地图pdf		   
            const pdf = new jsPDF('landscape', 'mm', [mapCanvas.width, mapCanvas.height]);
            pdf.addImage(
                mapCanvas.toDataURL('image/jpeg'),
                'JPEG',
                pdfImageX,
                pdfImageY,
                mapCanvas.width,
                mapCanvas.height
            );
            pdf.save('。。。.pdf')
            var image = mapCanvas.toDataURL('image/jpeg')
        });
        map.renderSync();
    }
}
export default generatePdf