(function() {
    'use strict';

    angular
        .module('newApp')
        .controller('CheckingDialogController', CheckingDialogController);

    CheckingDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Checking'];

    function CheckingDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Checking) {
        var vm = this;

        vm.checking = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.checking.id !== null) {
                Checking.update(vm.checking, onSaveSuccess, onSaveError);
            } else {
                Checking.save(vm.checking, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('newApp:checkingUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
