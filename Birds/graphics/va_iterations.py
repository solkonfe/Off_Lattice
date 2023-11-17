import sys
import plotly.graph_objects as go


def draw(dataOrder, iters):
    fig = go.Figure(
        data=go.Scatter(
            x=list(range(1, iters + 1)), y=dataOrder,
            mode='lines',
            marker={'color':'green'}
        ),
        layout=go.Layout(
            xaxis=dict(title='Iteraciones',titlefont=dict(size=20)),
            yaxis=dict(title='Orden',titlefont=dict(size=20)),
            plot_bgcolor='rgba(0,0,0,0)'
        )
    )

    # Set figure size
    fig.update_layout(width=1000, height=1000)
    fig.update_xaxes(
        mirror=True,
        ticks='outside',
        showline=True,
        linecolor='black',
        tickfont=dict(size=20),
    )
    fig.update_yaxes(
        mirror=True,
        ticks='outside',
        showline=True,
        linecolor='black',
        tickfont=dict(size=20),
    )
    fig.show()


def parseParameters(file):
    with open(file) as ordersFile:
        ordersLines = ordersFile.readlines()[1:]

    birds = []
    for line in ordersLines:
        newline = line.split('\t')[1:2]
        birds.append(float(newline[0]))

    return birds, len(birds)


if __name__ == '__main__':
    birds, iterations = parseParameters(sys.argv[1])
    draw(birds, iterations)
