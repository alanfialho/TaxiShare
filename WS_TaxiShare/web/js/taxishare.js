$(document).ready(function() {
    $('.pessoa.findall').on('click', function() {
        $.getJSON('ws/pessoa/findAll', function(data) {
            setData(data);
        });
    });

    $('.rota.findall').on('click', function() {
        $.getJSON('ws/rota/findAll', function(data) {
            setData(data);
        });
    });

    $('.pergunta.findall').on('click', function() {
        $.getJSON('ws/pergunta/findAll', function(data) {
            setData(data);
        });
    });

    $('.usuario.findall').on('click', function() {
        $.getJSON('ws/usuario/findAll', function(data) {
            setData(data);
        });
    });
});

function setData(data) {
    $('.json .erro').html(data.erro);
    $('.json .errorCode').html(data.errorCode);
    $('.json .descricao').html(data.descricao);
    $('.json .data').html(JSON.stringify(data.data));
}