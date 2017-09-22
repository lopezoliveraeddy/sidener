(function () {
    'use strict';

    angular
        .module('sidenerApp')
        .config(pagerConfig);

    pagerConfig.$inject = ['uibPagerConfig', 'paginationConstants'];

    function pagerConfig(uibPagerConfig, paginationConstants) {
        uibPagerConfig.itemsPerPage = paginationConstants.itemsPerPage;
        uibPagerConfig.previousText = '«';
        uibPagerConfig.nextText = '»';
    }
})();
