# Sistema de Gestão de Curso EAD 🎓

Este é um projeto desenvolvido em **Java** com interface gráfica **Swing**, projetado para gerenciar e visualizar a estrutura hierárquica de um Curso EAD. O sistema utiliza uma estrutura de árvore (`JTree`) para organizar o conteúdo em três níveis: **Trilhas**, **Módulos** e **Aulas**.

O projeto conta com persistência automática de dados localmente (em formato JSON), garantindo que todo o progresso e estrutura do curso sejam salvos entre as sessões.

---

## 🚀 Funcionalidades Implementadas

O sistema é um CRUD completo aplicado a uma estrutura de dados em árvore, com as seguintes funcionalidades:

* **Visualização Hierárquica:** Exibição clara da estrutura do curso usando `JTree`.
* **Adição Dinâmica:** Permite adicionar novas Trilhas (na raiz), Módulos (dentro de Trilhas) e Aulas (dentro de Módulos) identificando automaticamente o nó selecionado.
* **Edição In-Place:** Alteração rápida de nomes (Curso, Trilhas, Módulos) e informações de Aulas (título e duração em minutos).
* **Exclusão em Cascata:** Ao excluir um nó pai (como uma Trilha), todos os seus filhos (Módulos e Aulas) são removidos automaticamente, com aviso de confirmação de segurança.
* **Reordenação (Subir/Descer):** Permite alterar a ordem das Trilhas, Módulos ou Aulas diretamente na interface gráfica.
* **Busca de Conteúdo:** Varredura completa na árvore para encontrar termos específicos nos títulos, retornando o caminho exato do item encontrado.
* **Persistência de Dados (JSON):** Salvamento automático a cada alteração feita na interface usando a biblioteca **Gson**. O sistema sempre volta de onde parou.
* **Exportação de Relatórios:** Geração de arquivos `.txt` estruturados com tabulações.

---

## 🛠️ Tecnologias Utilizadas

* **Linguagem:** Java 17 (ou superior)
* **Interface Gráfica:** Java Swing (`JFrame`, `JTree`, `JOptionPane`)
* **Gerenciador de Dependências:** Maven
* **Bibliotecas Externas:** Google Gson (para manipulação e persistência JSON)

---

## ⚙️ Como Compilar e Executar no Apache NetBeans

Como o projeto foi construído utilizando **Maven**, o NetBeans o reconhece nativamente sem necessidade de configurações complexas. Siga os passos abaixo:

1. **Abra o Apache NetBeans.**
2. No menu superior, clique em **File** (Arquivo) > **Open Project...** (Abrir Projeto...).
3. Navegue até a pasta onde você clonou/baixou este repositório. O NetBeans mostrará a pasta `gestao-curso-EAD` com um pequeno ícone **[ma]** (indicando que é um projeto Maven).
4. Selecione a pasta e clique em **Open Project**.
5. Aguarde alguns segundos enquanto o NetBeans baixa as dependências (Gson) automaticamente pela internet (verifique a barra de progresso no canto inferior direito).
6. **Para Compilar:** Clique com o botão direito sobre o nome do projeto na aba *Projects* e selecione **Clean and Build** (Limpar e Construir).
7. **Para Executar:**
    * Clique com o botão direito sobre o projeto e selecione **Run** (Executar).
    * *Alternativa:* Navegue até `Source Packages` > `ifpar.tsi3.ead` > clique com o botão direito no arquivo **`Main.java`** e selecione **Run File** (Executar Arquivo).

---

## 💡 Exemplos de Uso

Abaixo estão alguns fluxos comuns de uso da interface:

* **Criando uma Estrutura:**
    1. Clique no nó principal ("Gestão de Curso - EAD").
    2. Clique em **Adicionar Filho** e digite "Programação Web".
    3. Na árvore, selecione "Programação Web", clique em **Adicionar Filho** novamente e digite "Módulo 1: HTML e CSS".
    4. Selecione o Módulo recém-criado, clique em **Adicionar Filho**, digite o nome da Aula e sua duração.
* **Reorganizando o Curso:**
    1. Selecione uma Aula que ficou na posição errada.
    2. Clique no botão **Subir (▲)** ou **Descer (▼)** para movê-la dentro do Módulo.
* **Recuperando de onde parou:**
    1. Feche o sistema no "X" da janela.
    2. Abra o programa novamente.
    3. Note que o arquivo `dados_sistema.json` (gerado na raiz do projeto) será lido e toda a árvore será recarregada automaticamente na tela.