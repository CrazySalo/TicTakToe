var app = angular.module('game', []);

app.controller('mainCtrl', function ($scope, $http) {

    $scope.figures = ["+", "x", "0"];
    $scope.vsAI = true;
    $scope.difficulty = 1;
    $scope.difficultyData = [
        { label: 'Easy', value: 0 },
        { label: 'Normal', value: 1 },
        { label: 'Impossible', value: 2}
      ];
    
    $scope.newGame = function() {
        $http.get("/rest/tictaktoe/new", {params: {vsAI: $scope.vsAI, difficulty: $scope.difficulty}}).then(function(response) {
            $scope.game = response.data;
        });
    };
    
    $scope.newGame();
    
    $scope.makeMove = function (x, y) {
        var move = {x: x, y: y, player: $scope.game.currentPlayerNumber};
        $http.post('/rest/tictaktoe/'+$scope.game.id, JSON.stringify(move))
             .then(function (response) {
                 $scope.game = response.data;
             });
    };
});