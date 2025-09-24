# Projeto_Gestao

Projeto Unisul

      1. Introdução:
          O documento atual foi pensando em instruir os requisitos de software para um
          Sistema de Gestão de Projetos e Equipes, com qual objetivo se tem de detalhar as
          funcionalidades características do sistema e suas limitações, dividindo em requisitos
          funcionais e não funcionais, servindo como guia para o desenvolvimento, e garantindo que
          os objetivos do projeto sejam atendidas.
      
      2. Requisitos Funcionais:
      
        2.1 Gestão de Usuários:
          ● O sistema deve permitir o cadastro de usuários com as seguintes
          informações: nome completo, CPF, e-mail, cargo, login e senha
          ● Cada usuário deve ter um perfil de acesso atribuído (administrador, gerente
          ou colaborador)
          ● O sistema deve permitir a edição e a exclusão dos dados de um usuário
          existente
      
        2.2 Gestão de Projetos:
          ● O sistema deve possibilitar o cadastro de projetos com os campos: nome do
          projeto, descrição, data de início, data de término prevista e status
          (planejado, em andamento, concluído, cancelado)
          ● Cada projeto deve ter um gerente responsável vinculado
          ● O sistema deve permitir a edição e a exclusão dos dados de um projeto
          ● O sistema deve permitir a alocação de equipes em projetos
          ● Um projeto pode ter mais de uma equipe envolvida
      
        2.3 Gestão de Equipes:
          ● O sistema deve permitir o cadastro de equipes com nome, descrição e
          membros (usuários vinculados)
          ● O sistema deve permitir a edição e a exclusão dos dados de uma equipe
          ● Uma equipe pode atuar em vários projetos
      
        2.4 Gestão de Tarefas:
          ● O sistema deve permitir o cadastro de tarefas para um projeto.
          ● As tarefas devem conter: título, descrição, projeto vinculado, responsável
          (usuário), status (pendente, em execução, concluída), data de início prevista,
          data de término prevista e datas reais de início e fim.
          ● Cada tarefa pertence a um único projeto.
          ● O sistema deve permitir a edição e a exclusão de tarefas.
      
        2.5 Relatórios e Dashboards:
          ● O sistema deve gerar relatórios de acompanhamento dos projetos
          ● O sistema deve fornecer um resumo do andamento dos projetos
          ● O sistema deve apresentar o desempenho de cada colaborador, exibindo as
          tarefas atribuídas e concluídas
          ● O sistema deve identificar e listar os projetos com risco de atraso 
          (data atual > data de término prevista)
      
        2.6 Autenticação e Acesso:
          ● O sistema deve ter uma tela de login
          ● A validação de login deve ser realizada no banco de dados
          ● O sistema deve gerenciar o acesso com base no perfil do usuário
          (administrador, gerente de projeto, colaborador)
      
      3. Requisitos Não Funcionais
      
        3.1 Usabilidade:
          ● A interface visual deve ser amigável e de fácil navegação, permitindo
          cadastro, edição e visualização intuitiva dos dados
          ● A interface deve ser prototipada antes da codificação
          ● O sistema deve fornecer mensagens de erro e validações claras para
          campos obrigatórios
      
        3.2 Desempenho:
          ● O sistema deve ter um tempo de resposta aceitável, mesmo com um grande
          volume de dados
      
        3.3 Segurança:
          ● O sistema deve garantir a segurança dos dados do usuário, especialmente
          senhas
          ● O sistema deve implementar controle de acesso para garantir que cada perfil
          de usuário possa alterar apenas o que é permitido
          ● O sistema deve registrar logs de acesso e atividades relevantes
      
        3.4 Tecnologia:
          ● O sistema deve ser desenvolvido em linguagem Java, com interface visual
          (Swing, JavaFX ou similar)
          ● O sistema deve estar conectado a um banco de dados MySQL
