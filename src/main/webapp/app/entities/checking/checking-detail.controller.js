(function() {
    'use strict';

    angular
        .module('newApp')
        .controller('CheckingDetailController', CheckingDetailController);

    CheckingDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Checking'];

    function CheckingDetailController($scope, $rootScope, $stateParams, previousState, entity, Checking) {
        var vm = this;

        vm.checking = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('newApp:checkingUpdate', function(event, result) {
            vm.checking = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
