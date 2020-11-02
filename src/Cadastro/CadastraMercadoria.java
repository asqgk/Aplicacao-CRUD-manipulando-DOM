package Cadastro;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;
import org.w3c.dom.*;
/**
 * @author Francisco dos Passos e Bruno Zampirom
 */

public class CadastraMercadoria {

    static public void main(String[] argv) {
        try {
            DocumentBuilderFactory b = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = b.newDocumentBuilder();
            Document myDoc = builder.parse("produtos.xml");
            Scanner leitura = new Scanner(System.in);
            String entrada;
            boolean loop = true; 

            while(loop) {
                System.out.println("1 - Consultar todos produtos");
                System.out.println("2 - Consultar um produto");
                System.out.println("3 - Adicionar um produto");
                System.out.println("4 - Remover um produto");
                System.out.println("5 - Atualizar um produto");
                System.out.println("6 - Sair");
                entrada = leitura.nextLine();
                switch (entrada) {
                    case "1":
                        consultaProdutos(myDoc, leitura);
                        break;
                    case "2":
                        consultaProduto(myDoc, leitura);
                        break;
                    case "3":
                        adiciona(myDoc, leitura);
                        break;
                    case "4":
                        remove(myDoc, leitura);
                        break;
                    case "5":
                        break;
                    case "6":
                        loop = false;
                        break;
                    default:
                        break;
                }
                System.out.println();
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private static Integer returnProdutoPosition(Document doc, String id) throws IOException, TransformerException {
        int pos = -1;
        NodeList nodos = doc.getDocumentElement().getChildNodes();
        //localiza posicao do cliente desejado no documento
        for (int i = 0; i < nodos.getLength(); i++) {
            if (nodos.item(i).getAttributes() != null && nodos.item(i).getAttributes().item(0).getNodeValue().equals(id))
                pos = i;
        }
        return pos;
    }

    private static void consultaProdutos(Document doc, Scanner leitura) throws IOException, TransformerException {
        NodeList nodos, nodo1;
        NamedNodeMap nodeID;
        int countProdutos = 0;
        System.out.println("-> Lista de produtos:");
        nodos = doc.getElementsByTagName("produto");
        for (int i = 0; i < nodos.getLength(); i++) {
            countProdutos++;
            nodo1 = nodos.item(i).getChildNodes();
            nodeID = nodos.item(i).getAttributes();
            System.out.println("--> ID: " + nodeID.item(0).getNodeValue());
            for (int j = 0; j < nodo1.getLength(); j++) {
                if (nodo1.item(j).getNodeName().equals("codigo_barras")){
                    System.out.println("---> Codigo de barras: " + nodo1.item(j).getFirstChild().getNodeValue());
                }
                if (nodo1.item(j).getNodeName().equals("descricao")){
                    System.out.println("---> Descricao: " + nodo1.item(j).getFirstChild().getNodeValue());
                }
                if (nodo1.item(j).getNodeName().equals("estoque_atual")){
                    System.out.println("---> Estoque: " + nodo1.item(j).getFirstChild().getNodeValue());
                }
                if (nodo1.item(j).getNodeName().equals("preco_unitario")){
                    System.out.println("---> Preco unitario: " + nodo1.item(j).getFirstChild().getNodeValue());
                }
            }
        }
        System.out.println("\n-> Total de produtos encontrados: " + countProdutos);
    }

    private static void consultaProduto(Document doc, Scanner leitura) throws IOException, TransformerException {
        NodeList nodos, nodo1;
        NamedNodeMap nodeID;
        String buscaID, idIteracao;
        Boolean encontrou = false;
        System.out.print("-> Informe o id do produto: ");
        buscaID = leitura.nextLine();
        nodos = doc.getElementsByTagName("produto");
        for (int i = 0; i < nodos.getLength(); i++) {
            nodeID = nodos.item(i).getAttributes();
            idIteracao = nodeID.item(0).getNodeValue().toString();
            if(buscaID.equals(idIteracao)) {
                encontrou = true;
                System.out.println("--> ID: " + buscaID);
                nodo1 = nodos.item(i).getChildNodes();
                for (int j = 0; j < nodo1.getLength(); j++) {
                    if (nodo1.item(j).getNodeName().equals("codigo_barras")){
                        System.out.println("---> Codigo de barras: " + nodo1.item(j).getFirstChild().getNodeValue());
                    }
                    if (nodo1.item(j).getNodeName().equals("descricao")){
                        System.out.println("---> Descricao: " + nodo1.item(j).getFirstChild().getNodeValue());
                    }
                    if (nodo1.item(j).getNodeName().equals("estoque_atual")){
                        System.out.println("---> Estoque: " + nodo1.item(j).getFirstChild().getNodeValue());
                    }
                    if (nodo1.item(j).getNodeName().equals("preco_unitario")){
                        System.out.println("---> Preco unitario: " + nodo1.item(j).getFirstChild().getNodeValue());
                    }
                }
                break;
            }
        }
        if (!encontrou) System.out.println("--> Nenhum produto encontrado!!"); 
    }

    private static void adiciona(Document doc, Scanner leitura) throws IOException, TransformerException {
        Element produtos, produto, codigo_barras, descricao, estoque_atual, preco_unitario;

        //obtem referencia do elemento produtos
        produtos = (Element) doc.getElementsByTagName("produtos").item(0);
        // define novo elemento cliente
        produto = doc.createElement("produto");

        //define atributo e adiciona ao elemento
        System.out.print("-> Informe o ID do produto: ");
        String id = leitura.nextLine();
        int pos = returnProdutoPosition(doc, id);
        if (pos != -1) {
            System.out.println("-> Já existe um produto cadastrado com o ID informado!");
            return;
        }
        produto.setAttribute("id", id);
        
        // define subelementos de produto
        codigo_barras = doc.createElement("codigo_barras"); 
        System.out.print("-> Informe o código de barras do produto: ");
        codigo_barras.appendChild(doc.createTextNode(leitura.nextLine()));
        produto.appendChild(codigo_barras);
        
        descricao = doc.createElement("descricao");        
        System.out.print("-> Informe a descrição do produto: ");
        descricao.appendChild(doc.createTextNode(leitura.nextLine()));
        produto.appendChild(descricao);
        
        estoque_atual = doc.createElement("estoque_atual");
        System.out.print("-> Informe o estoque atual do produto: ");
        estoque_atual.appendChild(doc.createTextNode(leitura.nextLine()));
        produto.appendChild(estoque_atual);
        
        preco_unitario = doc.createElement("preco_unitario");
        System.out.print("-> Informe o preço unitário do produto: ");
        preco_unitario.appendChild(doc.createTextNode(leitura.nextLine()));
        produto.appendChild(preco_unitario);

        /*
        //define elemento endereco e subelementos
        endereco = doc.createElement("endereco");
        logradouro = doc.createElement("logradouro");
        System.out.print("Informe o logradouro: ");
        logradouro.appendChild(doc.createTextNode(leitura.nextLine()));
        cidade = doc.createElement("cidade");
        System.out.print("Informe a cidade: ");
        cidade.appendChild(doc.createTextNode(leitura.nextLine()));
        estado = doc.createElement("estado");
        System.out.print("Informe o estado: ");
        estado.appendChild(doc.createTextNode(leitura.nextLine()));
        endereco.appendChild(logradouro);
        endereco.appendChild(cidade);
        endereco.appendChild(estado);
        cliente.appendChild(endereco);
        */

        //adiciona produto
        produtos.appendChild(produto);
        
        produtos.appendChild(doc.createComment("Arquivo gerado por CadastraMercadoria.java"));

        //serializa documento para arquivo
        XMLSerializer serializer = new XMLSerializer(
                new FileOutputStream("produtos.xml"), new OutputFormat(doc, "iso-8859-1", true));
        serializer.serialize(doc);
    }

    private static void remove(Document doc, Scanner leitura) throws IOException, TransformerException {

        System.out.print("-> Informe o ID do produto a remover: ");
        String numero = leitura.nextLine();

        int pos = returnProdutoPosition(doc, numero);
        // teste se localizou cliente
        if (pos == -1){
            System.out.println("-> Nenhum produto encontrado!");
            return;
        }

        // obtem referencia do elemento a excluir
        Element excluir = (Element) doc.getDocumentElement().getChildNodes().item(pos);

        // pede para o pai do nodo remover o filho passado por argumento
        excluir.getParentNode().removeChild(excluir);

        // remove comentario
        NodeList nl = doc.getDocumentElement().getChildNodes();
        System.out.println(pos);
        if (nl.item(pos + 1).getNodeType() == Element.COMMENT_NODE) {
            Comment comment=(Comment) nl.item(pos + 1);
            comment.getParentNode().removeChild(comment);
        }

        XMLSerializer serializer = new XMLSerializer(
                new FileOutputStream("produtos.xml"), new OutputFormat(doc, "iso-8859-1", true));
        serializer.serialize(doc);
        System.out.println("-> Produto removido com sucesso");
    }
}
